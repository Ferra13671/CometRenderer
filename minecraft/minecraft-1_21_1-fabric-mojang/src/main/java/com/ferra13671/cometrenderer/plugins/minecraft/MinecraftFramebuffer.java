package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class MinecraftFramebuffer implements Framebuffer {
    private final RenderTarget framebuffer;
    private final RenderColor clearColor;
    private final double clearDepth;

    public MinecraftFramebuffer(RenderTarget framebuffer, RenderColor clearColor, double clearDepth) {
        this.framebuffer = framebuffer;
        this.clearColor = clearColor;
        this.clearDepth = clearDepth;
    }

    @Override
    public void resize(int width, int height) {
        this.framebuffer.resize(width, height, false);
    }

    @Override
    public int getWidth() {
        return this.framebuffer.width;
    }

    @Override
    public int getHeight() {
        return this.framebuffer.height;
    }

    @Override
    public void bind(boolean setViewport) {
        this.framebuffer.bindWrite(setViewport);
    }

    @Override
    public int getColorTextureId() {
        return this.framebuffer.getColorTextureId();
    }

    @Override
    public int getDepthTextureId() {
        return this.framebuffer.useDepth ? this.framebuffer.getDepthTextureId() : -1;
    }

    @Override
    public void clearColor() {
        bind(false);
        GL11.glClearColor(
                this.clearColor.getColor()[0],
                this.clearColor.getColor()[1],
                this.clearColor.getColor()[2],
                this.clearColor.getColor()[3]
        );
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clearDepth() {
        if (this.framebuffer.useDepth) {
            bind(false);
            GL11.glClearDepth(this.clearDepth);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }
    }

    @Override
    public void clearAll() {
        clearColor();
        clearDepth();
    }

    @Override
    public void delete() {
        this.framebuffer.destroyBuffers();
    }
}
