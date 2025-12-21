package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.BufferUsage;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.allocator.Allocator;
import com.ferra13671.cometrenderer.buffer.allocator.IAllocator;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.IndexBufferGenerator;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;

/**
 * Основная реализация меша.
 *
 * @see IMesh
 * @see MeshBuilder
 */
public class Mesh implements IMesh {
    /** Формат вершин. **/
    private final VertexFormat vertexFormat;
    /** Количество вершин. **/
    private final int vertexCount;
    /** Количество индексов. **/
    private final int indexCount;
    /** Тип отрисовки вершин. **/
    private DrawMode drawMode;
    /** Буффер вершин, находящийся на GPU. **/
    private final GpuBuffer vertexBuffer;
    /**
     *  Автономный буффер вершин, созданный для данного меша.
     *  Данный буффер вершин будет создан только в том случае, когда сам меш становится автономным.
     */
    private GpuBuffer indexBuffer;
    /** Состояние автономности меша. **/
    private boolean standalone = false;

    /**
     * @param allocator аллокатор, в котором хранится буффер вершин, находящийся на CPU.
     * @param vertexFormat формат вершин.
     * @param vertexCount количество вершин.
     * @param indexCount количество индексов.
     * @param drawMode тип отрисовки вершин.
     */
    public Mesh(IAllocator allocator, VertexFormat vertexFormat, int vertexCount, int indexCount, DrawMode drawMode) {
        this.vertexFormat = vertexFormat;
        this.vertexCount = vertexCount;
        this.indexCount = indexCount;
        this.drawMode = drawMode;

        this.vertexBuffer = new GpuBuffer(allocator.getBuffer(), BufferUsage.STATIC_DRAW, BufferTarget.ARRAY_BUFFER);
    }

    /**
     * Делает меш автономным, создавая для него собственный буффер индексов.
     * Хотя обычные меши тоже могут использоваться как автономные, но автономная версия меша будет более оптимизированной в долгосрочной перспективе, чем обычная.
     */
    public Mesh makeStandalone() {
        if (!this.standalone) {
            this.standalone = true;
            recreateIndexBuffer();
        }

        return this;
    }

    private void recreateIndexBuffer() {
        if (this.indexBuffer != null)
            this.indexBuffer.close();

        IndexBufferGenerator indexBufferGenerator = this.drawMode.indexBufferGenerator();
        if (indexBufferGenerator != null)
            this.indexBuffer = indexBufferGenerator.getIndexBuffer(this.indexCount, true);
    }

    /**
     * Меняет тип отрисовки вершин меша на новый.
     * Используйте этот метод только в том случае, если знаете, что делаете.
     *
     * @param drawMode новый тип отрисовки вершин, который будет присвоен мешу.
     */
    public void changeDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
        if (this.standalone)
            recreateIndexBuffer();
    }

    @Override
    @OverriddenMethod
    public int getVertexCount() {
        return this.vertexCount;
    }

    @Override
    @OverriddenMethod
    public int getIndexCount() {
        return this.indexCount;
    }

    @Override
    @OverriddenMethod
    public GpuBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    @Override
    @OverriddenMethod
    public GpuBuffer getIndexBuffer() {
        return this.standalone ? this.indexBuffer : this.drawMode.indexBufferGenerator().getIndexBuffer(this.indexCount, false);
    }

    @Override
    @OverriddenMethod
    public DrawMode getDrawMode() {
        return this.drawMode;
    }

    @Override
    @OverriddenMethod
    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    @Override
    @OverriddenMethod
    public void close() {
        this.vertexBuffer.close();
        if (this.indexBuffer != null)
            this.indexBuffer.close();
    }

    /**
     * Создаёт новый сборщик меша с подготовленным аллокатором и размером.
     *
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершин.
     * @return новый сборщик меша.
     *
     * @see MeshBuilder
     */
    public static MeshBuilder builder(DrawMode drawMode, VertexFormat vertexFormat) {
        return builder(786432, drawMode, vertexFormat);
    }

    /**
     * Создаёт новый сборщик меша с подготовленным аллокатором и данным размером.
     *
     * @param size размер аллокатора.
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершин.
     * @return новый сборщик меша.
     *
     * @see MeshBuilder
     */
    public static MeshBuilder builder(int size, DrawMode drawMode, VertexFormat vertexFormat) {
        return builder(new Allocator(size), drawMode, vertexFormat, true);
    }

    /**
     * Создаёт новый сборщик меша с данным аллокатором.
     *
     * @param allocator аллокатор.
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершин.
     * @param closeAllocatorAfterBuild закрывать аллокатор после сборки меша или нет.
     * @return новый сборщик меша.
     *
     * @see MeshBuilder
     */
    public static MeshBuilder builder(IAllocator allocator, DrawMode drawMode, VertexFormat vertexFormat, boolean closeAllocatorAfterBuild) {
        return new MeshBuilder(allocator, drawMode, vertexFormat, closeAllocatorAfterBuild);
    }
}
