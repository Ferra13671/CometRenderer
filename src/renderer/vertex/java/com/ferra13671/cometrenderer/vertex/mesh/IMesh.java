package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;

public interface IMesh {

    int getIndexCount();

    GpuBuffer getVertexBuffer();

    GpuBuffer getIndexBuffer();

    DrawMode getDrawMode();

    VertexFormat getVertexFormat();

    void close();
}
