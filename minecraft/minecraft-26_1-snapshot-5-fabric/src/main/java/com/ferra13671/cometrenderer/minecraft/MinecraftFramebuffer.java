package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;

public class MinecraftFramebuffer implements Framebuffer {
    private final RenderTarget framebuffer;
    private final Color clearColor;
    private final double clearDepth;

    public MinecraftFramebuffer(RenderTarget framebuffer, Color clearColor, double clearDepth) {
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
        return this.framebuffer.width;
    }

    @Override
    public int getHeight() {
        return this.framebuffer.height;
    }

    @Override
    public void bind(boolean setViewport) {
        int id = ((GlTexture) this.framebuffer.getColorTexture()).getFbo(
                ((CRMController) CRM.getController()).getDirectStateAccess(),
                this.framebuffer.useDepth ? this.framebuffer.getDepthTexture() : null
        );

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
        if (setViewport)
            GL11.glViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getColorTextureId() {
        return ((GlTexture) this.framebuffer.getColorTexture()).glId();
    }

    @Override
    public int getDepthTextureId() {
        return this.framebuffer.useDepth ? ((GlTexture) this.framebuffer.getDepthTexture()).glId() : -1;
    }

    @Override
    public void clearColor() {
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.framebuffer.getColorTexture(), this.clearColor.getRGB());
    }

    @Override
    public void clearDepth() {
        if (this.framebuffer.useDepth)
            RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.framebuffer.getDepthTexture(), this.clearDepth);
    }

    @Override
    public void clearAll() {
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.framebuffer.getColorTexture(), this.clearColor.getRGB(), this.framebuffer.getDepthTexture(), this.clearDepth);
    }

    @Override
    public void delete() {
        this.framebuffer.destroyBuffers();
    }
}
