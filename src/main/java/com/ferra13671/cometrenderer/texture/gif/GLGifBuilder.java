package com.ferra13671.cometrenderer.texture.gif;

import com.ferra13671.cometrenderer.texture.TextureFiltering;
import com.ferra13671.cometrenderer.texture.TextureWrapping;
import com.ferra13671.cometrenderer.texture.loader.GifLoader;
import com.ferra13671.cometrenderer.utils.Builder;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public class GLGifBuilder<T> extends Builder<GLGif> {
    private String name = null;
    private T path = null;
    private final GifLoader<T> loader;
    private TextureFiltering filtering = null;
    private TextureWrapping wrapping = null;

    GLGifBuilder(GifLoader<T> loader) {
        super("gif");
        this.loader = loader;
    }

    public GLGifBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public GLGifBuilder<T> info(T path) {
        this.path = path;
        return this;
    }

    public GLGifBuilder<T> filtering(TextureFiltering filtering) {
        this.filtering = filtering;
        return this;
    }

    public GLGifBuilder<T> wrapping(TextureWrapping wrapping) {
        this.wrapping = wrapping;
        return this;
    }

    public GLGif build() {
        try {
            assertNotNull(this.name, "name");
            assertNotNull(this.loader, "loader");
            assertNotNull(this.path, "path");

            GLGif glGif = new GLGif(this.name, this.loader.load(this.path));

            glGif.setFiltering(this.filtering);
            glGif.setWrapping(this.wrapping);

            return glGif;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
