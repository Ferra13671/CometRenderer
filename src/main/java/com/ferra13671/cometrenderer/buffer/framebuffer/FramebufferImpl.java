package com.ferra13671.cometrenderer.buffer.framebuffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.texture.ColorMode;
import com.ferra13671.cometrenderer.texture.GLTexture;
import com.ferra13671.cometrenderer.texture.TextureFiltering;
import com.ferra13671.cometrenderer.texture.TextureWrapping;
import com.ferra13671.cometrenderer.texture.GLTextureBuilder;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;

public class FramebufferImpl implements Framebuffer {
    private final String name;
    @Getter
    private final boolean useDepth;
    @Getter
    private final boolean useStencil;
    private final Color clearColor;
    private final double clearDepth;
    private final int clearStencil;
    @Getter
    private final int id;
    @Getter
    protected GLTexture colorTexture;
    @Getter
    protected GLTexture depthAndStencilTexture;

    public FramebufferImpl(FramebufferInfo framebufferInfo) {
        this.id = CometRenderer.getDevice().createFramebuffer();

        this.name = framebufferInfo.getName();
        this.useDepth = framebufferInfo.isUseDepth();
        this.useStencil = framebufferInfo.isUseStencil();
        this.clearColor = framebufferInfo.getClearColor();
        this.clearDepth = framebufferInfo.getClearDepth();
        this.clearStencil = framebufferInfo.getClearStencil();

        resize(framebufferInfo.getWidth(), framebufferInfo.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        deleteTextures();

        setColorTexture(
                GLTextureBuilder.empty()
                        .name(this.name + "[Color]")
                        .info(width, height)
                        .filtering(TextureFiltering.DEFAULT)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build()
        );
        if (isUseDepth()) {
            setDepthAndStencilTexture(
                    GLTextureBuilder.empty()
                            .name(this.name + "[Depth]")
                            .info(width, height, isUseStencil() ? ColorMode.DEPTH_AND_STENCIL : ColorMode.DEPTH)
                            .filtering(TextureFiltering.DEFAULT)
                            .wrapping(TextureWrapping.DEFAULT)
                            .build()
            );
        }
    }

    @API(status = API.Status.INTERNAL, since = "3.0")
    public void setColorTexture(GLTexture colorTexture) {
        this.colorTexture = colorTexture;
        CometRenderer.getDevice().getDirectStateManager().attachFramebufferTexture(this, GL30.GL_COLOR_ATTACHMENT0, colorTexture);
    }

    @API(status = API.Status.INTERNAL, since = "3.0")
    public void setDepthAndStencilTexture(GLTexture depthAndStencilTexture) {
        if (isUseDepth()) {
            this.depthAndStencilTexture = depthAndStencilTexture;
            CometRenderer.getDevice().getDirectStateManager().attachFramebufferTexture(this, isUseStencil() ? GL30.GL_DEPTH_STENCIL_ATTACHMENT : GL30.GL_DEPTH_ATTACHMENT, depthAndStencilTexture);
        }
    }

    @Override
    public int getColorTextureId() {
        return this.colorTexture != null ? this.colorTexture.getId() : -1;
    }

    @Override
    public int getDepthAndStencilTextureId() {
        return this.depthAndStencilTexture != null ? this.depthAndStencilTexture.getId() : -1;
    }

    @Override
    public int getWidth() {
        return this.colorTexture.getWidth();
    }

    @Override
    public int getHeight() {
        return this.colorTexture.getHeight();
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
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void clearDepth() {
        bind(false);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void clearStencil() {
        bind(false);
        GL11.glClearStencil(this.clearStencil);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void clearAll() {
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClearStencil(this.clearStencil);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
    }

    private void deleteTextures() {
        deleteColor();
        deleteDepthAndStencil();
    }

    private void deleteColor() {
        if (this.colorTexture != null) {
            this.colorTexture.delete();
            this.colorTexture = null;
        }
    }

    private void deleteDepthAndStencil() {
        if (this.depthAndStencilTexture != null) {
            this.depthAndStencilTexture.delete();
            this.depthAndStencilTexture = null;
        }
    }

    @Override
    public void delete() {
        deleteTextures();
        CometRenderer.getDevice().deleteFramebuffer(getId());
    }
}
