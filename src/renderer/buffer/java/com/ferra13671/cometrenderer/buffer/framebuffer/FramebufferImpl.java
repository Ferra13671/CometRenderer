package com.ferra13671.cometrenderer.buffer.framebuffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import lombok.Getter;
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
        this.id = CometRenderer.getDevice().getDirectStateManager().createFramebuffer();

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
                TextureLoader.INPUT_STREAM.createTextureBuilder()
                        .name(this.name + "[Color]")
                        .info(width, height)
                        .filtering(TextureFiltering.DEFAULT)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build()
        );
        if (isUseDepth()) {
            setDepthAndStencilTexture(
                    TextureLoader.INPUT_STREAM.createTextureBuilder()
                            .name(this.name + "[Depth]")
                            .info(width, height, isUseStencil() ? ColorMode.DEPTH_AND_STENCIL : ColorMode.DEPTH)
                            .filtering(TextureFiltering.DEFAULT)
                            .wrapping(TextureWrapping.DEFAULT)
                            .build()
            );
        }
    }

    public void setColorTexture(GLTexture colorTexture) {
        deleteColor();
        this.colorTexture = colorTexture;

        CometRenderer.getDevice().getDirectStateManager().attachFramebufferTexture(this, GL30.GL_COLOR_ATTACHMENT0, colorTexture);
    }

    public void setDepthAndStencilTexture(GLTexture depthAndStencilTexture) {
        if (isUseDepth()) {
            deleteDepthAndStencil();
            this.depthAndStencilTexture = depthAndStencilTexture;

            CometRenderer.getDevice().getDirectStateManager().attachFramebufferTexture(this, GL30.GL_DEPTH_STENCIL_ATTACHMENT, depthAndStencilTexture);
        }
    }

    @Override
    public int getColorTextureId() {
        return this.colorTexture != null ? this.colorTexture.getTexId() : -1;
    }

    @Override
    public int getDepthAndStencilTextureId() {
        return this.depthAndStencilTexture != null ? this.depthAndStencilTexture.getTexId() : -1;
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
        State.FRAMEBUFFER.bindFramebuffer(this.id, setViewport, getWidth(), getHeight());
    }

    @Override
    public void clearColor() {
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
    }

    @Override
    public void clearDepth() {
        bind(false);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
    }

    @Override
    public void clearStencil() {
        bind(false);
        GL11.glClearStencil(this.clearStencil);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
    }

    @Override
    public void clearAll() {
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClearStencil(this.clearStencil);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
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

        GL30.glDeleteFramebuffers(this.id);
    }
}
