package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;

/**
 * Объект, хранящий в себе буффер вершин и различную информацию для него.
 */
public interface IMesh extends AutoCloseable {

    /**
     * Возвращает количество индексов для буффера вершин.
     *
     * @return количество индексов для буффера вершин.
     */
    int getIndexCount();

    /**
     * Возвращает буффер вершин.
     *
     * @return буффер вершин.
     */
    GpuBuffer getVertexBuffer();

    /**
     * Возвращает буффер индексов.
     *
     * @return буффер индексов.
     */
    GpuBuffer getIndexBuffer();

    /**
     * Возвращает тип отрисовки вершин.
     *
     * @return тип отрисовки вершин.
     */
    DrawMode getDrawMode();

    /**
     * Возвращает формат вершин.
     *
     * @return формат вершин.
     */
    VertexFormat getVertexFormat();

    /**
     * Закрывает меш.
     */
    @Override
    @OverriddenMethod
    void close();
}
