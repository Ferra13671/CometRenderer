package com.ferra13671.cometrenderer.vertex.builder;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import net.minecraft.client.util.BufferAllocator;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.util.stream.Collectors;

import static com.mojang.blaze3d.vertex.VertexFormat.IndexType;

/*
 * Билдер вершин. Используемый для построения буффера вершин для последующего рендеринга.
 */
public class VertexBuilder {
    //Максимальное количество вершин
    private static final int MAX_VERTICES = 16777215;

    //Аллокатор данных для вершин
    private final BufferAllocator bufferAllocator;
    private final DrawMode drawMode;
    private final VertexFormat vertexFormat;
    private final int vertexSize;
    private final int[] elementOffsets;
    private final int requiredMask;

    private long vertexPointer = -1L;
    private int vertexCount;
    private int currentMask;
    private boolean building = true;

    private VertexBuilder(BufferAllocator bufferAllocator, DrawMode drawMode, VertexFormat vertexFormat) {
        this.bufferAllocator = bufferAllocator;
        this.drawMode = drawMode;
        this.vertexFormat = vertexFormat;
        this.vertexSize = vertexFormat.getVertexSize();
        this.elementOffsets = vertexFormat.getElementOffsets();
        this.requiredMask = vertexFormat.getElementsMask() & ~1;
    }

    //TODO сделать исключение VertexBuilderNotBuildingException
    private void ensureBuilding() {
        if (!this.building) {
            throw new IllegalStateException("Not building!");
        }
    }

    public BuiltVertexBuffer endNullable() {
        this.ensureBuilding();
        this.endVertex();
        BuiltVertexBuffer builtBuffer = this.build();
        bufferAllocator.close();
        this.building = false;
        this.vertexPointer = -1L;
        return builtBuffer;
    }

    public BuiltVertexBuffer end() {
        BuiltVertexBuffer builtBuffer = this.endNullable();
        if (builtBuffer == null) {
            //TODO сделать исключение EmptyVertexBuilderException
            throw new IllegalStateException("VertexBuilder was empty");
        } else {
            return builtBuffer;
        }
    }

    private BuiltVertexBuffer build() {
        if (this.vertexCount == 0) {
            return null;
        } else {
            BufferAllocator.CloseableBuffer closeableBuffer = this.bufferAllocator.getAllocated();
            if (closeableBuffer == null) {
                return null;
            } else {
                int i = this.drawMode.getIndexCount(this.vertexCount);
                IndexType indexType = IndexType.smallestFor(this.vertexCount);
                return new BuiltVertexBuffer(closeableBuffer.getBuffer(), this.vertexFormat, this.vertexCount, i, this.drawMode, indexType);
            }
        }
    }

    private long beginVertex() {
        this.ensureBuilding();
        this.endVertex();
        if (this.vertexCount >= MAX_VERTICES) {
            //TODO сделать исключение IllegalVertexCountException
            throw new IllegalStateException("Trying to write too many vertices (>16777215) into BufferBuilder");
        } else {
            this.vertexCount++;
            long l = this.bufferAllocator.allocate(this.vertexSize);
            this.vertexPointer = l;
            return l;
        }
    }

    private void endVertex() {
        if (this.vertexCount != 0 && this.currentMask != 0) {
            String string = vertexFormat.getElementsFromMask(this.currentMask).map(this.vertexFormat::getVertexElementName).collect(Collectors.joining(", "));
            //TODO сделать исключение BadVertexStructureException
            throw new IllegalStateException("Missing elements in vertex: " + string);
        }
    }

    private long beginElement(VertexElement element) {
        int i = this.currentMask;
        int j = i & ~element.mask();
        if (j == i) {
            return -1L;
        } else {
            this.currentMask = j;
            long l = this.vertexPointer;
            if (l == -1L) {
                //TODO сделать исключение VertexBuilderNotBuildingVertexException
                throw new IllegalArgumentException("Not currently building vertex");
            } else {
                return l + this.elementOffsets[element.getId()];
            }
        }
    }

    public VertexBuilder vertex(float x, float y, float z) {
        long l = this.beginVertex() + this.elementOffsets[0];
        this.currentMask = this.requiredMask;
        MemoryUtil.memPutFloat(l, x);
        MemoryUtil.memPutFloat(l + 4L, y);
        MemoryUtil.memPutFloat(l + 8L, z);
        return this;
    }

    public VertexBuilder vertex(Matrix4f matrix4f, float x, float y, float z) {
        Vector3f vec = matrix4f.transformPosition(x, y, z, new Vector3f());
        return vertex(vec.x, vec.y, vec.z);
    }

    public <T> VertexBuilder element(String name, VertexElementType<T> elementType, T... values) {
        long pointer = beginElement(vertexFormat.getVertexElement(name));
        if (pointer != -1L)
            elementType.uploadConsumer().accept(pointer, values);

        return this;
    }

    public static VertexBuilder create(DrawMode drawMode, VertexFormat vertexFormat) {
        return create(786432, drawMode, vertexFormat);
    }

    public static VertexBuilder create(int size, DrawMode drawMode, VertexFormat vertexFormat) {
        return new VertexBuilder(new BufferAllocator(size), drawMode, vertexFormat);
    }
}
