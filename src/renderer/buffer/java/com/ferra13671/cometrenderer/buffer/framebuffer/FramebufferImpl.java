package com.ferra13671.cometrenderer.buffer.framebuffer;

import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;

public class FramebufferImpl implements Framebuffer {
    private final String name;
    @Getter
    private final boolean useDepth;
    private final Color clearColor;
    private final double clearDepth;
    private final int id;
    @Getter
    protected GLTexture colorTexture;
    @Getter
    protected GLTexture depthTexture;

    public FramebufferImpl(@NonNull String name, boolean useDepth, int width, int height, @NonNull Color clearColor, double clearDepth) {
        this.name = name;
        this.useDepth = useDepth;
        this.id = GL30.glGenFramebuffers();
        this.clearColor = clearColor;
        this.clearDepth = clearDepth;

        resize(width, height);
    }

    @Override
    public void resize(int width, int height) {
        deleteTextures();

        setColorTexture(
                TextureLoaders.INPUT_STREAM.createTextureBuilder()
                        .name(this.name + "[Color]")
                        .info(width, height)
                        .filtering(TextureFiltering.DEFAULT)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build()
        );
        if (isUseDepth()) {
            setDepthTexture(
                    TextureLoaders.INPUT_STREAM.createTextureBuilder()
                            .name(this.name + "[Depth]")
                            .info(width, height, ColorMode.DEPTH)
                            .filtering(TextureFiltering.DEFAULT)
                            .wrapping(TextureWrapping.DEFAULT)
                            .build()
            );
        }
    }

    public void setColorTexture(GLTexture colorTexture) {
        this.colorTexture = colorTexture;
        bind(false);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture != null ? colorTexture.getTexId() : 0, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void setDepthTexture(GLTexture depthTexture) {
        if (isUseDepth()) {
            this.depthTexture = depthTexture;
            bind(false);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture != null ? depthTexture.getTexId() : 0, 0);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }
    }

    @Override
    public int getColorTextureId() {
        return this.colorTexture != null ? this.colorTexture.getTexId() : -1;
    }

    @Override
    public int getDepthTextureId() {
        return this.depthTexture != null ? this.depthTexture.getTexId() : -1;
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
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.id);
        if (setViewport)
            GL11.glViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public void clearColor() {
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clearDepth() {
        bind(false);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clearAll() {
        bind(false);
        GL11.glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        GL11.glClearDepth(this.clearDepth);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    private void deleteTextures() {
        if (this.colorTexture != null)
            this.colorTexture.delete();
        if (this.depthTexture != null)
            this.depthTexture.delete();
    }

    @Override
    public void delete() {
        deleteTextures();

        GL30.glDeleteFramebuffers(this.id);
    }
}
