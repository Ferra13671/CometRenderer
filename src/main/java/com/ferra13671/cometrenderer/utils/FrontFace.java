package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

@AllArgsConstructor
public enum FrontFace {
    Clockwise(GL11.GL_CW),
    CounterClockwise(GL11.GL_CCW);

    public final int glId;
}
