package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.gltextureutils.GlTex;

import java.nio.ByteBuffer;

public interface DirectStateManager {

    int createFramebuffer();

    int createBuffer();

    int createVertexArray();

    void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GlTex texture);

    void bufferData(GpuBuffer buffer, long size);

    void bufferData(GpuBuffer buffer, ByteBuffer data);

    void enableVertexAttributeArray(int vertBufId, int index);

    void vertexAttributeFormat(int vertBufId, int attribIndex, int size, int type, boolean normalized, int relativeOffset);

    void vertexAttributeIntFormat(int vertBufId, int attribIndex, int size, int type, int relativeOffset);

    void vertexAttributeBinding(int vertBufId, int attribIndex, int bindingIndex);
}
