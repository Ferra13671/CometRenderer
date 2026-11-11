package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.AllArgsConstructor;
import org.lwjgl.glfw.GLFW;

@AllArgsConstructor
public enum CursorMode {
    Normal(GLFW.GLFW_CURSOR_NORMAL),
    Hidden(GLFW.GLFW_CURSOR_HIDDEN),
    Disabled(GLFW.GLFW_CURSOR_DISABLED);

    public final int id;
}
