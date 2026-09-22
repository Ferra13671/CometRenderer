package com.ferra13671.cometrenderer.texture.atlas;

import com.ferra13671.cometrenderer.texture.GLTexture;
import org.apiguardian.api.API;

import java.util.HashMap;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record TextureAtlasScheme(int textureWidth, int textureHeight, HashMap<GLTexture, TexturePos> poses) {
}
