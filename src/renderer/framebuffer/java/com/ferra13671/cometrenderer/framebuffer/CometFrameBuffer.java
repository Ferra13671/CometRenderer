package com.ferra13671.cometrenderer.framebuffer;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;

import java.awt.*;

/*
 * Фреймбуффер, имеющий в себе дополнительные методы и информацию
 */
public class CometFrameBuffer extends Framebuffer {
    private static final int TRANSLUCENT = new Color(0f, 0f, 0f, 0f).hashCode();
    private final int clearColor;

    public CometFrameBuffer(String name, boolean useDepth) {
        this(name, 1, 1, useDepth);
    }

    public CometFrameBuffer(String name, int width, int height, boolean useDepth) {
        this(name, width, height, TRANSLUCENT, useDepth);
    }

    public CometFrameBuffer(String name, int width, int height, int clearColor, boolean useDepth) {
        super(name, useDepth);
        this.resize(width, height);
        this.clearColor = clearColor;
    }

    /*
     * Очищает текстуру цвета фреймбуффера
     */
    public void clearColorTexture() {
        if (this.colorAttachment != null)
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.colorAttachment, this.clearColor);
    }

    /*
     * Очищает текстуру грубины фреймбуффера
     */
    public void clearDepthTexture() {
        if (this.depthAttachment != null)
            RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.depthAttachment, 0f);
    }

    /*
     * Очищает все текстуры фреймбуффера
     */
    public void clearAllTextures() {
        clearColorTexture();
        clearDepthTexture();
    }

    /*
     * Биндит фреймбуффер
     */
    public void bind(boolean setViewport) {
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, FrameBufferUtils.getFrameBufferId(this.colorAttachmentView, this.depthAttachmentView));

        if (setViewport)
            GlStateManager._viewport(0, 0, this.colorAttachment.getWidth(0), this.colorAttachment.getHeight(0));
    }
}
