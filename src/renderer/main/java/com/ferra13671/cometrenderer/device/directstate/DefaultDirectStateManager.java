package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.gltextureutils.GlTex;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class DefaultDirectStateManager implements DirectStateManager {

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
    public void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GlTex texture) {
        framebuffer.bind(false);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, texture == null ? 0 : texture.getTexId(), 0);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
    }

    @Override
    public void bufferData(GpuBuffer buffer, long size) {
        buffer.bind();
        GL15.glBufferData(buffer.getTarget().glId, size, buffer.getUsage().glId);
        buffer.unbind();
    }

    @Override
    public void bufferData(GpuBuffer buffer, ByteBuffer data) {
        buffer.bind();
        GL15.glBufferData(buffer.getTarget().glId, data, buffer.getUsage().glId);
        buffer.unbind();
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
}
