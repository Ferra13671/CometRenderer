package com.ferra13671.cometrenderer.device.directstate;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.gltextureutils.GlTex;
import org.lwjgl.opengl.ARBDirectStateAccess;

import java.nio.ByteBuffer;

public class ARBDirectStateManager implements DirectStateManager {

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
    public void attachFramebufferTexture(Framebuffer framebuffer, int attachment, GlTex texture) {
        ARBDirectStateAccess.glNamedFramebufferTexture(framebuffer.getId(), attachment, texture == null ? 0 : texture.getTexId(), 0);
    }

    @Override
    public void bufferData(GpuBuffer buffer, long size) {
        ARBDirectStateAccess.glNamedBufferData(buffer.getId(), size, buffer.getUsage().glId);
    }

    @Override
    public void bufferData(GpuBuffer buffer, ByteBuffer data) {
        ARBDirectStateAccess.glNamedBufferData(buffer.getId(), data, buffer.getUsage().glId);
    }
}
