package com.ferra13671.cometrenderer.texture;

import org.apiguardian.api.API;

import java.nio.ByteBuffer;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record GLTextureInfo(ByteBuffer pixels, int width, int height, boolean usingStb) {
}
