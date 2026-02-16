package com.ferra13671.cometrenderer.minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class VAOState {
    private static int state = 0;

    public static void save() {
        state = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
    }

    public static void load() {
        GL30.glBindVertexArray(state);
    }
}
