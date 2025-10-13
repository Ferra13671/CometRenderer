package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import net.minecraft.client.gl.GlGpuBuffer;

import java.util.HashMap;

/*
 * Привязыватель структуры вершин к буфферу вершин
 */
public abstract class VertexFormatBufferUploader {
    protected final HashMap<VertexFormat, VertexFormatBuffer> vertexFormatBuffers = new HashMap<>();

    public abstract void applyFormatToBuffer(GlGpuBuffer vertexBuffer, VertexFormat vertexFormat);
}
