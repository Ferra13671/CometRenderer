package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFW;

@API(status = API.Status.MAINTAINED, since = "3.1")
@AllArgsConstructor
public enum CursorMode {
    Normal(GLFW.GLFW_CURSOR_NORMAL),
    Hidden(GLFW.GLFW_CURSOR_HIDDEN),
    Disabled(GLFW.GLFW_CURSOR_DISABLED);

    public final int id;
}
