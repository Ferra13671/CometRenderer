package com.ferra13671.cometrenderer.texture;

import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public class GLTextureBuilder<T> {
    private String name = null;
    private GLTextureInfo info = null;
    private final TextureLoader<T> loader;
    private ColorMode colorMode = ColorMode.RGBA;
    private TextureFiltering filtering = null;
    private TextureWrapping wrapping = null;

    public GLTextureBuilder(TextureLoader<T> loader) {
        this.loader = loader;
    }

    public GLTextureBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public GLTextureBuilder<T> info(T path) {
        return info(path, this.colorMode);
    }

    public GLTextureBuilder<T> info(T path, ColorMode colorMode) {
        try {
            this.info = this.loader.load(path, colorMode);
            this.colorMode = colorMode;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return this;
    }

    public GLTextureBuilder<T> info(int width, int height) {
        return info(width, height, this.colorMode);
    }

    public GLTextureBuilder<T> info(int width, int height, ColorMode colorMode) {
        this.info = new GLTextureInfo(null, width, height, false);
        this.colorMode = colorMode;
        return this;
    }

    public GLTextureBuilder<T> colorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        return this;
    }

    public GLTextureBuilder<T> filtering(TextureFiltering filtering) {
        this.filtering = filtering;
        return this;
    }

    public GLTextureBuilder<T> wrapping(TextureWrapping wrapping) {
        this.wrapping = wrapping;
        return this;
    }

    public GLTexture build() {
        try {
            checkArguments();

            GLTexture texture = GLTexture.of(this.name, this.colorMode, this.info);

            texture.setFiltering(this.filtering);
            texture.setWrapping(this.wrapping);

            return texture;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private void checkArguments() {
        if (this.name == null)
            throw new IllegalArgumentException("Name cannot be null.");
        if (this.info == null)
            throw new IllegalArgumentException(String.format("Texture information in texture '%s' cannot be null", this.name));
        if (this.colorMode == null)
            throw new IllegalArgumentException(String.format("ColorMode in texture '%s' cannot be null.", this.name));
    }

    public static GLTextureBuilder<?> empty() {
        return new GLTextureBuilder<>(null);
    }
}
