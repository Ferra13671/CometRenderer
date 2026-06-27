package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.BufferUsage;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.allocator.Allocator;
import com.ferra13671.cometrenderer.buffer.allocator.IAllocator;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.Getter;

/**
 * Основная реализация меша.
 *
 * @see IMesh
 * @see MeshBuilder
 */
public class Mesh implements IMesh {
    /** Формат вершин. **/
    @Getter
    private final VertexFormat vertexFormat;
    /** Количество вершин. **/
    @Getter
    private final int vertexCount;
    /** Количество индексов. **/
    @Getter
    private final int indexCount;
    /** Тип отрисовки вершин. **/
    @Getter
    private DrawMode drawMode;
    /** Буффер вершин, находящийся на GPU. **/
    @Getter
    private final GpuBuffer vertexBuffer;

    /**
     * @param allocator аллокатор, в котором хранится буффер вершин, находящийся на CPU.
     * @param vertexFormat формат вершин.
     * @param vertexCount количество вершин.
     * @param drawMode тип отрисовки вершин.
     */
    public Mesh(IAllocator allocator, VertexFormat vertexFormat, int vertexCount, DrawMode drawMode) {
        this.vertexFormat = vertexFormat;
        this.vertexCount = vertexCount;
        this.indexCount = drawMode.getIndexCount(vertexCount);
        this.drawMode = drawMode;

        this.vertexBuffer = new GpuBuffer(allocator.getBuffer(), BufferUsage.STATIC_DRAW, BufferTarget.ARRAY_BUFFER, true);
    }

    /**
     * Меняет тип отрисовки вершин меша на новый.
     * Используйте этот метод только в том случае, если знаете, что делаете.
     *
     * @param drawMode новый тип отрисовки вершин, который будет присвоен мешу.
     */
    public void changeDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    @Override
    public void close() {
        this.vertexBuffer.close();
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
        return builder(CometRenderer.getConfig().DEFAULT_MESH_ALLOCATOR_SIZE.getValue(), drawMode, vertexFormat);
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
