package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

@AllArgsConstructor
public enum Face {
    Front(GL11.GL_FRONT, true, false),
    Back(GL11.GL_BACK, false, true),
    FrontAndBack(GL11.GL_FRONT_AND_BACK, true, true);

    public final int glId;
    public final boolean front;
    public final boolean back;
}
