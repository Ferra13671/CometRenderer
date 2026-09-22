package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.texture.GLTex;

import java.nio.ByteBuffer;

public interface DirectStateManager {

    int createTexture();

    int createFramebuffer();

    int createBuffer();

    int createVertexArray();

    int createSampler();

    void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GLTex texture);

    void blitFramebuffer(
            Framebuffer srcFramebuffer, Framebuffer dstFramebuffer,
            int srcX, int srcY, int srcWidth, int srcHeight,
            int dstX, int dstY, int dstWidth, int dstHeight,
            int mask, int filter
    );

    void blitFramebuffer(
            int srcFramebufferId, int dstFramebufferId,
            int srcX, int srcY, int srcWidth, int srcHeight,
            int dstX, int dstY, int dstWidth, int dstHeight,
            int mask, int filter
    );

    void bufferData(GpuBuffer buffer, long size);

    void bufferData(GpuBuffer buffer, ByteBuffer data);

    void bufferStorage(GpuBuffer buffer, ByteBuffer data, int flags);

    void enableVertexAttributeArray(int vertBufId, int index);

    void vertexAttributeFormat(int vertBufId, int attribIndex, int size, int type, boolean normalized, int relativeOffset);

    void vertexAttributeIntFormat(int vertBufId, int attribIndex, int size, int type, int relativeOffset);

    void vertexAttributeBinding(int vertBufId, int attribIndex, int bindingIndex);

    void textureStorage(GLTex texture);

    void textureImage(GLTex texture, int x, int y, ByteBuffer pixels);

    void copyTexture(GLTex texture, int x, int y, int width, int height);

    void copyTextureToBuffer(GLTex texture, ByteBuffer targetBuffer);

    void textureParameterInt(GLTex texture, int paramId, int value);

    void textureParameterFloat(GLTex texture, int paramId, float value);
}
