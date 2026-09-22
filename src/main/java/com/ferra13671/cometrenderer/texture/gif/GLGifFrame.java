package com.ferra13671.cometrenderer.texture.gif;

import com.ferra13671.cometrenderer.texture.GLTexture;
import org.apiguardian.api.API;

import java.awt.image.BufferedImage;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record GLGifFrame(GLTexture texture, BufferedImage image, int delay) {
}
