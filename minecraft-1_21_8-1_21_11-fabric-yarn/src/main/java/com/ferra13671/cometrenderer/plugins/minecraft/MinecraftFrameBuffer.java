package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;

public class MinecraftFrameBuffer implements Framebuffer {
    private final net.minecraft.client.gl.Framebuffer framebuffer;
    private final Color clearColor;
    private final double clearDepth;

    public MinecraftFrameBuffer(net.minecraft.client.gl.Framebuffer framebuffer, Color clearColor, double clearDepth) {
        this.framebuffer = framebuffer;
        this.clearColor = clearColor;
        this.clearDepth = clearDepth;
    }

    @Override
    public void resize(int width, int height) {
        this.framebuffer.resize(width, height);
    }

    @Override
    public int getWidth() {
        return this.framebuffer.textureWidth;
    }

    @Override
    public int getHeight() {
        return this.framebuffer.textureHeight;
    }

    @Override
    public void bind(boolean setViewport) {
        int id = ((GlTexture) this.framebuffer.getColorAttachment()).getOrCreateFramebuffer(
                ((GlBackend) RenderSystem.getDevice()).getBufferManager(),
                this.framebuffer.useDepthAttachment ? this.framebuffer.getDepthAttachment() : null
        );

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
        if (setViewport)
            GL11.glViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getColorTextureId() {
        return ((GlTexture) this.framebuffer.getColorAttachment()).getGlId();
    }

    @Override
    public int getDepthTextureId() {
        return this.framebuffer.useDepthAttachment ? ((GlTexture) this.framebuffer.getDepthAttachment()).getGlId() : -1;
    }

    @Override
    public void clearColor() {
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.framebuffer.getColorAttachment(), this.clearColor.getRGB());
    }

    @Override
    public void clearDepth() {
        if (this.framebuffer.useDepthAttachment)
            RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.framebuffer.getDepthAttachment(), this.clearDepth);
    }

    @Override
    public void clearAll() {
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.framebuffer.getColorAttachment(), this.clearColor.getRGB(), this.framebuffer.getDepthAttachment(), this.clearDepth);
    }

    @Override
    public void delete() {
        this.framebuffer.delete();
    }
}
