package com.ferra13671.cometrenderer.texture;

import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import com.ferra13671.cometrenderer.utils.Builder;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public class GLTextureBuilder<T> extends Builder<GLTexture> {
    private String name = null;
    private GLTextureInfo info = null;
    private final TextureLoader<T> loader;
    private ColorMode colorMode = ColorMode.RGBA;
    private TextureFiltering filtering = null;
    private TextureWrapping wrapping = null;

    GLTextureBuilder(TextureLoader<T> loader) {
        super("texture");
        this.loader = loader;
    }

    public GLTextureBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public GLTextureBuilder<T> info(T path) {
        if (path != null) {
            if (this.loader == null)
                throw new UnsupportedOperationException("Cannot load texture info from path without loader.");

            try {
                this.info = this.loader.load(path, colorMode);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }

        return this;
    }

    public GLTextureBuilder<T> info(int width, int height) {
        this.info = new GLTextureInfo(null, width, height, false);
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

    @Override
    public GLTexture build() {
        try {
            assertNotNull(this.name, "name");
            assertNotNull(this.info, "info");
            assertNotNull(this.colorMode, "color mode");

            GLTexture texture = new GLTexture(this.name, this.colorMode, this.info);

            texture.setFiltering(this.filtering);
            texture.setWrapping(this.wrapping);

            return texture;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
