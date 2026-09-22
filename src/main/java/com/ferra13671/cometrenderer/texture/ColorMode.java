package com.ferra13671.cometrenderer.texture;

import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record ColorMode(int externalFormatId, int internalFormatId, int pixelSize, int dataType) {
    public static final ColorMode RGB = new ColorMode(GL11.GL_RGB, GL11.GL_RGB8, 3, GL11.GL_UNSIGNED_BYTE);
    public static final ColorMode RGBA = new ColorMode(GL11.GL_RGBA, GL11.GL_RGBA8, 4, GL11.GL_UNSIGNED_BYTE);
    public static final ColorMode DEPTH = new ColorMode(GL30.GL_DEPTH_COMPONENT, GL30.GL_DEPTH_COMPONENT32, 1, GL11.GL_UNSIGNED_INT);
    public static final ColorMode DEPTH_AND_STENCIL = new ColorMode(GL30.GL_DEPTH_STENCIL, GL30.GL_DEPTH24_STENCIL8, 1, GL30.GL_UNSIGNED_INT_24_8);
}