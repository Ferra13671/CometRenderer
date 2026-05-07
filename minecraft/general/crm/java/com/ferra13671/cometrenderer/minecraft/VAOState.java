package com.ferra13671.cometrenderer.minecraft;

import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@API(status = API.Status.INTERNAL)
@UtilityClass
public class VAOState {
    private int state = 0;

    public void save() {
        state = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
    }

    public void load() {
        GL30.glBindVertexArray(state);
    }
}
