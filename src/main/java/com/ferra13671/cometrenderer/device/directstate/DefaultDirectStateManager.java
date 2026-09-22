package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.texture.ColorMode;
import com.ferra13671.cometrenderer.texture.GLTex;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class DefaultDirectStateManager implements DirectStateManager {

    @Override
    public int createTexture() {
        return GL11.glGenTextures();
    }

    @Override
    public int createFramebuffer() {
        return GL30.glGenFramebuffers();
    }

    @Override
    public int createBuffer() {
        return GL15.glGenBuffers();
    }

    @Override
    public int createVertexArray() {
        return GL30.glGenVertexArrays();
    }

    @Override
    public int createSampler() {
        int sampler = GL33.glGenSamplers();
        CometRenderer.getDevice().getPipelineStateManager().ensureTextureUnit(0);
        int prevSampler = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);

        GL33.glBindSampler(0, sampler);
        GL33.glBindSampler(0, prevSampler);

        return sampler;
    }

    @Override
    public void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GLTex texture) {
        framebuffer.bind(false);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, texture == null ? 0 : texture.getId(), 0);
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
    public void blitFramebuffer(int srcFramebufferId, int dstFramebufferId, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight, int mask, int filter) {
        CometRenderer.getDevice().getPipelineStateManager().setReadFramebuffer(srcFramebufferId);
        CometRenderer.getDevice().getPipelineStateManager().setDrawFramebuffer(dstFramebufferId);

        GL30.glBlitFramebuffer(
                srcX, srcY, srcWidth, srcHeight,
                dstX, dstY, dstWidth, dstHeight,
                mask, filter
        );
    }

    @Override
    public void bufferData(GpuBuffer buffer, long size) {
        buffer.bind();
        GL15.glBufferData(buffer.getTarget().glId, size, buffer.getUsage().glId);
    }

    @Override
    public void bufferData(GpuBuffer buffer, ByteBuffer data) {
        buffer.bind();
        GL15.glBufferData(buffer.getTarget().glId, data, buffer.getUsage().glId);
    }

    @Override
    public void bufferStorage(GpuBuffer buffer, ByteBuffer data, int flags) {
        buffer.bind();
        ARBBufferStorage.glBufferStorage(buffer.getTarget().glId, data, flags);
    }

    @Override
    public void enableVertexAttributeArray(int vertBufId, int index) {
        GL30.glEnableVertexAttribArray(index);
    }

    @Override
    public void vertexAttributeFormat(int vertBufId, int attribIndex, int size, int type, boolean normalized, int relativeOffset) {
        ARBVertexAttribBinding.glVertexAttribFormat(attribIndex, size, type, normalized, relativeOffset);
    }

    @Override
    public void vertexAttributeIntFormat(int vertBufId, int attribIndex, int size, int type, int relativeOffset) {
        ARBVertexAttribBinding.glVertexAttribIFormat(attribIndex, size, type, relativeOffset);
    }

    @Override
    public void vertexAttributeBinding(int vertBufId, int attribIndex, int bindingIndex) {
        ARBVertexAttribBinding.glVertexAttribBinding(attribIndex, bindingIndex);
    }

    @Override
    public void textureStorage(GLTex texture) {
        texture.bind();
        ColorMode colorMode = texture.getColorMode();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, colorMode.internalFormatId(), texture.getWidth(), texture.getHeight(), 0, colorMode.externalFormatId(), colorMode.dataType(), (ByteBuffer) null);
    }

    @Override
    public void textureImage(GLTex texture, int x, int y, ByteBuffer pixels) {
        texture.bind();
        ColorMode colorMode = texture.getColorMode();
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, texture.getWidth(), texture.getHeight(), colorMode.externalFormatId(), colorMode.dataType(), pixels);
    }

    @Override
    public void copyTexture(GLTex texture, int x, int y, int width, int height) {
        texture.bind();
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, x, y, width, height);
    }

    @Override
    public void copyTextureToBuffer(GLTex texture, ByteBuffer targetBuffer) {
        texture.bind();
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, texture.getColorMode().externalFormatId(), texture.getColorMode().dataType(), targetBuffer);
    }

    @Override
    public void textureParameterInt(GLTex texture, int paramId, int value) {
        texture.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, paramId, value);
    }

    @Override
    public void textureParameterFloat(GLTex texture, int paramId, float value) {
        texture.bind();
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, paramId, value);
    }
}
