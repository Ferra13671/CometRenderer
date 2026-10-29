package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL14;

@AllArgsConstructor
public enum BlendEquation {
    ADD(GL14.GL_FUNC_ADD),
    MIN(GL14.GL_MIN),
    MAX(GL14.GL_MAX),
    SUBTRACT(GL14.GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT(GL14.GL_FUNC_REVERSE_SUBTRACT);

    public final int glId;
}
