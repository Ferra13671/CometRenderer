package com.ferra13671.cometrenderer.vertex.builder;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static com.mojang.blaze3d.vertex.VertexFormat.IndexType;

public class BuiltVertexBuffer {
    private final VertexFormat vertexFormat;
    private final int vertexCount;
    private final int indexCount;
    private final DrawMode drawMode;
    private final IndexType indexType;
    private final ByteBuffer vertexBuffer;

    public BuiltVertexBuffer(ByteBuffer byteBuffer, VertexFormat vertexFormat, int vertexCount, int indexCount, DrawMode drawMode, IndexType indexType) {
        this.vertexFormat = vertexFormat;
        this.vertexCount = vertexCount;
        this.indexCount = indexCount;
        this.drawMode = drawMode;
        this.indexType = indexType;

        ByteBuffer buffer = MemoryUtil.memAlloc(byteBuffer.capacity());
        MemoryUtil.memCopy(byteBuffer, buffer);
        this.vertexBuffer = buffer;
    }

    public void close() {
        MemoryUtil.memFree(vertexBuffer);
    }

    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public ByteBuffer getVertexBuffer() {
        return vertexBuffer;
    }
}
