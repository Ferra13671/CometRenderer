package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;

import java.util.concurrent.atomic.AtomicReference;

public record VertexFormatBuffer(int glId, VertexFormat vertexFormat, AtomicReference<GpuBuffer> buffer) {}
