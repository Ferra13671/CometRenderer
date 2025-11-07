package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.BufferUsage;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import net.minecraft.client.util.BufferAllocator;

import java.nio.ByteBuffer;

/**
 * Основная реализация меша.
 *
 * @see IMesh
 */
public class Mesh implements IMesh {
    /** Формат вершин. **/
    private final VertexFormat vertexFormat;
    /** Количество вершин. **/
    private final int vertexCount;
    /** Количество индексов. **/
    private final int indexCount;
    /** Тип отрисовки вершин. **/
    private final DrawMode drawMode;
    /** Буффер вершин, находящийся на GPU. **/
    private final GpuBuffer vertexBuffer;

    /**
     * @param byteBuffer буффер вершин, находящийся на CPU.
     * @param vertexFormat формат вершин.
     * @param vertexCount количество вершин.
     * @param indexCount количество индексов.
     * @param drawMode тип отрисовки вершин.
     * @param afterInitRunnable действие, выполняемое после инициализации меша.
     */
    public Mesh(ByteBuffer byteBuffer, VertexFormat vertexFormat, int vertexCount, int indexCount, DrawMode drawMode, Runnable afterInitRunnable) {
        this.vertexFormat = vertexFormat;
        this.vertexCount = vertexCount;
        this.indexCount = indexCount;
        this.drawMode = drawMode;

        this.vertexBuffer = new GpuBuffer(byteBuffer, BufferUsage.STATIC_DRAW, BufferTarget.ARRAY_BUFFER);

        afterInitRunnable.run();
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
        return this.drawMode.indexBufferGenerator().getIndexBuffer(this.indexCount);
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
        return builder(new BufferAllocator(size), drawMode, vertexFormat, true);
    }

    /**
     * Создаёт новый сборщик меша с данным аллокатором.
     *
     * @param bufferAllocator аллокатор.
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершин.
     * @param closeAllocatorAfterBuild закрывать аллокатор после сборки меша или нет.
     * @return новый сборщик меша.
     *
     * @see MeshBuilder
     */
    public static MeshBuilder builder(BufferAllocator bufferAllocator, DrawMode drawMode, VertexFormat vertexFormat, boolean closeAllocatorAfterBuild) {
        return new MeshBuilder(bufferAllocator, drawMode, vertexFormat, closeAllocatorAfterBuild);
    }
}
