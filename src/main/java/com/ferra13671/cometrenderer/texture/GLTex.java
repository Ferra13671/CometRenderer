package com.ferra13671.cometrenderer.texture;

import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public interface GLTex {

    void bind();

    TextureFiltering getFiltering();

    void setFiltering(TextureFiltering textureFiltering);

    TextureWrapping getWrapping();

    ColorMode getColorMode();

    void setWrapping(TextureWrapping textureWrapping);

    int getWidth();

    int getHeight();

    int getId();

    void delete();
}
