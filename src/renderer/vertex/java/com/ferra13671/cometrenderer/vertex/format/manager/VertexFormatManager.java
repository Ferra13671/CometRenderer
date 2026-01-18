package com.ferra13671.cometrenderer.vertex.format.manager;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;

/**
 * Менеджер установки формата вершины к буфферу вершин.
 */
public abstract class VertexFormatManager {

    /**
     * Устанавливает формат вершины к буфферу вершин.
     *
     * @param vertexBuffer буффер вершин.
     * @param vertexFormat формат вершины.
     */
    public abstract void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat);

    protected abstract VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat);
}
