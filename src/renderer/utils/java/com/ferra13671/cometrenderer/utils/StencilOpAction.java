package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

@AllArgsConstructor
public enum StencilOpAction {
    KEEP(GL11.GL_KEEP),
    REPLACE(GL11.GL_REPLACE),
    INCR(GL11.GL_INCR),
    INCR_WRAP(GL14.GL_INCR_WRAP),
    DECR(GL11.GL_DECR),
    DECR_WRAP(GL14.GL_DECR_WRAP);

    public final int glId;
}
