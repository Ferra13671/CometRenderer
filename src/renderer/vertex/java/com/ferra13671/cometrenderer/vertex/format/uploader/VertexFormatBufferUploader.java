package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;

import java.util.HashMap;

/*
 * Привязыватель структуры вершин к буфферу вершин
 */
public abstract class VertexFormatBufferUploader {
    protected final HashMap<VertexFormat, VertexFormatBuffer> vertexFormatBuffers = new HashMap<>();

    public abstract void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat);
}
