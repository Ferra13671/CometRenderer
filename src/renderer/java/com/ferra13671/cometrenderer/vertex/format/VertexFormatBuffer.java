package com.ferra13671.cometrenderer.vertex.format;

import net.minecraft.client.gl.GlGpuBuffer;

import java.util.concurrent.atomic.AtomicReference;

public record VertexFormatBuffer(int glId, VertexFormat vertexFormat, AtomicReference<GlGpuBuffer> buffer) {}
