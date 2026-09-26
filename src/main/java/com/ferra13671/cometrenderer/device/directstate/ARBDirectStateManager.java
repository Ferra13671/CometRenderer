package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.texture.ColorMode;
import com.ferra13671.cometrenderer.texture.GLTex;
import org.lwjgl.opengl.ARBDirectStateAccess;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class ARBDirectStateManager implements DirectStateManager {

    @Override
    public int createTexture() {
        return ARBDirectStateAccess.glCreateTextures(GL11.GL_TEXTURE_2D);
    }

    @Override
    public int createFramebuffer() {
        return ARBDirectStateAccess.glCreateFramebuffers();
    }

    @Override
    public int createBuffer() {
        return ARBDirectStateAccess.glCreateBuffers();
    }

    @Override
    public int createVertexArray() {
        return ARBDirectStateAccess.glCreateVertexArrays();
    }

    @Override
    public int createSampler() {
        return ARBDirectStateAccess.glCreateSamplers();
    }

    @Override
    public void blitFramebuffer(Framebuffer srcFramebuffer, Framebuffer dstFramebuffer, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight, int mask, int filter) {
        blitFramebuffer(
                srcFramebuffer == null ? 0 : srcFramebuffer.getId(),
                dstFramebuffer == null ? 0 : dstFramebuffer.getId(),
                srcX, srcY, srcWidth, srcHeight,
                dstX, dstY, dstWidth, dstHeight,
                mask, filter
        );
    }

    @Override
    public void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GLTex texture) {
        ARBDirectStateAccess.glNamedFramebufferTexture(framebuffer.getId(), attachment, texture == null ? 0 : texture.getId(), 0);
    }

    @Override
    public void blitFramebuffer(int srcFramebufferId, int dstFramebufferId, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight, int mask, int filter) {
        ARBDirectStateAccess.glBlitNamedFramebuffer(
                srcFramebufferId, dstFramebufferId,
                srcX, srcY, srcWidth, srcHeight,
                dstX, dstY, dstWidth, dstHeight,
                mask, filter
        );
    }

    @Override
    public void bufferData(GpuBuffer buffer, long size) {
        ARBDirectStateAccess.glNamedBufferData(buffer.getId(), size, buffer.getUsage().glId);
    }

    @Override
    public void bufferData(GpuBuffer buffer, ByteBuffer data) {
        ARBDirectStateAccess.glNamedBufferData(buffer.getId(), data, buffer.getUsage().glId);
    }

    @Override
    public void bufferStorage(GpuBuffer buffer, ByteBuffer data, int flags) {
        ARBDirectStateAccess.glNamedBufferStorage(buffer.getId(), data, flags);
    }

    @Override
    public void enableVertexAttributeArray(int vertBufId, int index) {
        ARBDirectStateAccess.glEnableVertexArrayAttrib(vertBufId, index);
    }

    @Override
    public void vertexAttributeFormat(int vertBufId, int attribIndex, int size, int type, boolean normalized, int relativeOffset) {
        ARBDirectStateAccess.glVertexArrayAttribFormat(vertBufId, attribIndex, size, type, normalized, relativeOffset);
    }

    @Override
    public void vertexAttributeIntFormat(int vertBufId, int attribIndex, int size, int type, int relativeOffset) {
        ARBDirectStateAccess.glVertexArrayAttribIFormat(vertBufId, attribIndex, size, type, relativeOffset);
    }

    @Override
    public void vertexAttributeBinding(int vertBufId, int attribIndex, int bindingIndex) {
        ARBDirectStateAccess.glVertexArrayAttribBinding(vertBufId, attribIndex, bindingIndex);
    }

    @Override
    public void textureStorage(GLTex texture) {
        ColorMode colorMode = texture.getColorMode();
        ARBDirectStateAccess.glTextureStorage2D(texture.getId(), 1, colorMode.internalFormatId(), texture.getWidth(), texture.getHeight());
    }

    @Override
    public void textureImage(GLTex texture, ByteBuffer pixels) {
        textureImage(texture, 0, 0, texture.getWidth(), texture.getHeight(), pixels);
    }

    @Override
    public void textureImage(GLTex texture, int x, int y, int width, int height, ByteBuffer pixels) {
        ColorMode colorMode = texture.getColorMode();
        ARBDirectStateAccess.glTextureSubImage2D(texture.getId(), 0, x, y, width, height, colorMode.externalFormatId(), colorMode.dataType(), pixels);
    }

    @Override
    public void copyTexture(GLTex texture, int x, int y, int width, int height) {
        ARBDirectStateAccess.glCopyTextureSubImage2D(texture.getId(), 0, 0, 0, x, y, width, height);
    }

    @Override
    public void copyTextureToBuffer(GLTex texture, ByteBuffer targetBuffer) {
        ARBDirectStateAccess.glGetTextureImage(texture.getId(), 0, texture.getColorMode().externalFormatId(), texture.getColorMode().dataType(), targetBuffer);
    }

    @Override
    public void textureParameterInt(GLTex texture, int paramId, int value) {
        ARBDirectStateAccess.glTextureParameteri(texture.getId(), paramId, value);
    }

    @Override
    public void textureParameterFloat(GLTex texture, int paramId, float value) {
        ARBDirectStateAccess.glTextureParameterf(texture.getId(), paramId, value);
    }
}
