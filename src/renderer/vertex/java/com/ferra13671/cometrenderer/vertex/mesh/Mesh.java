package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.BufferAllocator;

import java.nio.ByteBuffer;

public class Mesh implements IMesh {
    private final VertexFormat vertexFormat;
    private final int indexCount;
    private final DrawMode drawMode;
    private final GpuBuffer vertexBuffer;

    public Mesh(ByteBuffer byteBuffer, VertexFormat vertexFormat, int indexCount, DrawMode drawMode, Runnable afterInitRunnable) {
        this.vertexFormat = vertexFormat;
        this.indexCount = indexCount;
        this.drawMode = drawMode;

        this.vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex buffer", 40, byteBuffer);

        afterInitRunnable.run();
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
        return this.drawMode.shapeIndexBuffer.getIndexBuffer(this.indexCount);
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

    public static MeshBuilder builder(DrawMode drawMode, VertexFormat vertexFormat) {
        return builder(786432, drawMode, vertexFormat);
    }

    public static MeshBuilder builder(int size, DrawMode drawMode, VertexFormat vertexFormat) {
        return builder(new BufferAllocator(size), drawMode, vertexFormat, true);
    }

    public static MeshBuilder builder(BufferAllocator bufferAllocator, DrawMode drawMode, VertexFormat vertexFormat, boolean closeAllocatorAfterBuild) {
        return new MeshBuilder(bufferAllocator, drawMode, vertexFormat, closeAllocatorAfterBuild);
    }
}
