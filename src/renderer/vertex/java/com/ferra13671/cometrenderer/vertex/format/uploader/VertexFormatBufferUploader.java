package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;

import java.util.HashMap;

/**
 * Установщик формата вершины к буфферу вершин.
 */
public abstract class VertexFormatBufferUploader {

    public abstract void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat);

    public abstract VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat);
}
