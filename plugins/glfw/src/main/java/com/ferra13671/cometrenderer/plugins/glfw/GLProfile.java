package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFW;

@API(status = API.Status.STABLE, since = "3.0")
@AllArgsConstructor
public enum GLProfile {
    Any(GLFW.GLFW_OPENGL_ANY_PROFILE),
    Core(GLFW.GLFW_OPENGL_CORE_PROFILE),
    Compatibility(GLFW.GLFW_OPENGL_COMPAT_PROFILE);

    public final int glfwId;
}
