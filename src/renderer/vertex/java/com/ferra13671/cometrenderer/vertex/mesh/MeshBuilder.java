package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.allocator.IAllocator;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.BadVertexStructureException;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.IllegalMeshBuilderStateException;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.VertexOverflowException;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.util.stream.Collectors;

/**
 * Основная реализация сборщика меша.
 *
 * @see IMeshBuilder
 * @see Mesh
 */
public class MeshBuilder extends Builder<Mesh> implements IMeshBuilder<MeshBuilder, Mesh> {
    /** Максимальное количество вершин в меше. **/
    //TODO Config.MAX_MESH_VERTEX_COUNT
    public static final int MAX_VERTICES = 16777215;

    /** Аллокатор. **/
    private final IAllocator allocator;
    /** Тип отрисовки вершин. **/
    private final DrawMode drawMode;
    /** Формат вершин. **/
    private final VertexFormat vertexFormat;
    /** Размер вершины. **/
    private final int vertexSize;
    /** Массив отступов для элементов вершин. **/
    private final int[] elementOffsets;
    /** Маска полноценной вершины. **/
    private final int requiredMask;
    /** Закрывать аллокатор после сборки меша или нет. **/
    private final boolean closeAllocatorAfterBuild;

    private long vertexPointer = -1L;
    /** Количество вершин в сборщике. **/
    private int vertexCount;
    /** Текущая маска вершины. **/
    private int currentMask;
    /** Закрыт сборщик меша или нет. **/
    //TODO Rename to 'built'
    private boolean closed = false;

    /**
     * @param bufferAllocator аллокатор.
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершин.
     * @param closeAllocatorAfterBuild закрывать аллокатор после сборки меша или нет.
     */
    public MeshBuilder(IAllocator bufferAllocator, DrawMode drawMode, VertexFormat vertexFormat, boolean closeAllocatorAfterBuild) {
        super("mesh");
        this.allocator = bufferAllocator;
        this.drawMode = drawMode;
        this.vertexFormat = vertexFormat;
        this.vertexSize = vertexFormat.getVertexSize();
        this.elementOffsets = vertexFormat.getElementOffsets();
        this.requiredMask = vertexFormat.getElementsMask() & ~1;
        this.closeAllocatorAfterBuild = closeAllocatorAfterBuild;
    }

    /**
     * Проверяет, собирается ли меш данным сборщиком или нет.
     * Если нет, то вызывается ошибка.
     */
    //TODO rename to assertNotBuilt
    private void ensureBuilding() {
        if (this.closed) {
            CometRenderer.manageException(new IllegalMeshBuilderStateException(
                    "Attempt to interact with MeshBuilder, which does not building.",
                    new String[]{
                            "You are trying to use MeshBuilder after it has been built."
                    },
                    new String[]{
                            "Check your build or usage MeshBuilder method and fix it."
                    }
            ));
        }
    }

    @Override
    public Mesh buildNullable() {
        this.ensureBuilding();
        this.endVertex();
        Mesh builtBuffer = this.buildInternal();
        if (this.closeAllocatorAfterBuild)
            this.allocator.close();
        this.closed = true;
        this.vertexPointer = -1L;
        return builtBuffer;
    }

    @Override
    public Mesh build() {
        Mesh builtBuffer = this.buildNullable();
        if (builtBuffer == null) {
            CometRenderer.manageException(new IllegalMeshBuilderStateException(
                    "MeshBuilder was empty.",
                    new String[]{
                            "You haven't built any vertices in MeshBuilder and called MeshBuilder build via the 'buildThrowable' method, which throw an exception about the MeshBuilder being empty."
                    },
                    new String[]{
                            "If your rendering method assumes an empty MeshBuilder, call the builder via the 'buildNullable' method. If not, check your MeshBuilder method and fix it."
                    }
            ));
            return null;
        } else {
            return builtBuffer;
        }
    }

    /**
     * Собирает вершины и их элементы в цельный меш.
     *
     * @return цельный меш.
     */
    private Mesh buildInternal() {
        if (this.vertexCount == 0) {
            return null;
        } else {
            if (this.allocator.isEmpty()) {
                return null;
            } else {
                int i = this.drawMode.indexCountFunction().apply(this.vertexCount);
                return new Mesh(this.allocator, this.vertexFormat, this.vertexCount, i, this.drawMode);
            }
        }
    }

    /**
     * Начинает сборку новой вершины.
     *
     * @return адрес новой вершины.
     */
    private long beginVertex() {
        this.ensureBuilding();
        this.endVertex();
        if (this.vertexCount >= MAX_VERTICES) {
            CometRenderer.manageException(new VertexOverflowException());
            return -1L;
        } else {
            this.vertexCount++;
            long l = this.allocator.allocate(this.vertexSize);
            this.vertexPointer = l;
            return l;
        }
    }

    /**
     * Заканчивает сборку текущей вершины.
     */
    private void endVertex() {
        if (this.vertexCount != 0 && this.currentMask != 0) {
            String string = vertexFormat.getElementsFromMask(this.currentMask).map(this.vertexFormat::getVertexElementName).collect(Collectors.joining(", "));
            CometRenderer.manageException(new BadVertexStructureException(string));
        }
    }

    /**
     * Начинает сборку элемента вершины.
     *
     * @param element элемент вершины.
     * @return адрес элемента.
     */
    private long beginElement(VertexElement element) {
        int i = this.currentMask;
        int j = i & ~element.mask();
        if (j == i) {
            return -1L;
        } else {
            this.currentMask = j;
            long l = this.vertexPointer;
            if (l == -1L) {
                CometRenderer.manageException(new IllegalMeshBuilderStateException(
                        "Not currently building vertex.",
                        new String[]{
                                "You are trying to add data to vertex that has already been built."
                        },
                        new String[]{
                                "Check your vertex building method and fix it."
                        }
                ));
                return -1L;
            } else {
                return l + this.elementOffsets[element.getId()];
            }
        }
    }

    @Override
    public MeshBuilder vertex(float x, float y, float z) {
        long l = this.beginVertex() + this.elementOffsets[0];
        this.currentMask = this.requiredMask;
        MemoryUtil.memPutFloat(l, x);
        MemoryUtil.memPutFloat(l + 4L, y);
        MemoryUtil.memPutFloat(l + 8L, z);
        return this;
    }

    @Override
    public MeshBuilder vertex(Matrix4f matrix4f, float x, float y, float z) {
        Vector3f vec = matrix4f.transformPosition(x, y, z, new Vector3f());
        return vertex(vec.x, vec.y, vec.z);
    }

    @Override
    public <T> MeshBuilder element(String name, VertexElementType<T> elementType, T... values) {
        long pointer = beginElement(vertexFormat.getVertexElement(name));
        if (pointer != -1L)
            elementType.uploadConsumer().accept(pointer, values);

        return this;
    }
}
