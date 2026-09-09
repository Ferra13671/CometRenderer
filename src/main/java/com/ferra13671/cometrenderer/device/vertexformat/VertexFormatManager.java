package com.ferra13671.cometrenderer.device.vertexformat;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

/**
 * Менеджер установки формата вершины к буфферу вершин.
 */
public interface VertexFormatManager {

    /**
     * Устанавливает формат вершины к буфферу вершин.
     *
     * @param vertexBuffer буффер вершин.
     * @param vertexFormat формат вершины.
     */
    void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat);
}
