package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

@AllArgsConstructor
public enum PolygonMode {
    Point(GL11.GL_POINT),
    Line(GL11.GL_LINE),
    Fill(GL11.GL_FILL);

    public final int glId;
}
