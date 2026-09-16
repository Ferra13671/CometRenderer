package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MinecraftFramebuffer implements Framebuffer {
    private final RenderTarget framebuffer;
    private final Color clearColor;
    private final double clearDepth;
    private final int clearStencil;

    public MinecraftFramebuffer(RenderTarget framebuffer, Color clearColor, double clearDepth, int clearStencil) {
        this.framebuffer = framebuffer;
        this.clearColor = clearColor;
        this.clearDepth = clearDepth;
        this.clearStencil = clearStencil;
    }

    @Override
    public void resize(int width, int height) {
        this.framebuffer.resize(width, height);
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
    public int getId() {
        return ((GlTexture) this.framebuffer.getColorTexture()).getFbo(
                ((CRMController) CRM.controller).getDirectStateAccess(),
                this.framebuffer.useDepth ? this.framebuffer.getDepthTexture() : null
        );
    }

    @Override
    public void bind(boolean setViewport) {
        CometRenderer.getDevice().getPipelineStateManager().setFramebuffer(getId());
        setViewport(setViewport);
    }

    @Override
    public void bindRead() {
        CometRenderer.getDevice().getPipelineStateManager().setReadFramebuffer(getId());
    }

    @Override
    public void bindDraw(boolean setViewport) {
        CometRenderer.getDevice().getPipelineStateManager().setDrawFramebuffer(getId());
        setViewport(setViewport);
    }

    void setViewport(boolean viewport) {
        if (viewport)
            CometRenderer.getDevice().getPipelineStateManager().setViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getColorTextureId() {
        return ((GlTexture) this.framebuffer.getColorTexture()).glId();
    }

    @Override
    public int getDepthAndStencilTextureId() {
        return this.framebuffer.useDepth ? ((GlTexture) this.framebuffer.getDepthTexture()).glId() : -1;
    }

    @Override
    public void blit(Framebuffer target, boolean copyDepth, boolean copyStencil) {
        CometRenderer.getDevice().blitFramebuffer(
                getId(), getWidth(), getHeight(),
                target.getId(), target.getWidth(), target.getHeight(),
                copyDepth, copyStencil
        );
    }

    @Override
    public void blit(int targetFramebufferId, int width, int height, boolean copyDepth, boolean copyStencil) {
        CometRenderer.getDevice().blitFramebuffer(
                getId(), getWidth(), getHeight(),
                targetFramebufferId, width, height,
                copyDepth, copyStencil
        );
    }

    @Override
    public void clearColor() {
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.framebuffer.getColorTexture(), this.clearColor.getRGB());
    }

    @Override
    public void clearDepth() {
        if (this.framebuffer.useDepth) {
            bind(false);
            GL11.glClearDepth(this.clearDepth);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        }
    }

    @Override
    public void clearStencil() {
        bind(false);
        GL11.glClearStencil(this.clearStencil);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void clearAll() {
        clearStencil();
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.framebuffer.getColorTexture(), this.clearColor.getRGB(), this.framebuffer.getDepthTexture(), this.clearDepth);
    }

    @Override
    public void delete() {
        this.framebuffer.destroyBuffers();
    }
}
