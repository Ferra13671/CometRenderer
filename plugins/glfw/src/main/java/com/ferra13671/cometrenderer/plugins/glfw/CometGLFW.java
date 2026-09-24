package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.plugins.glfw.key.KeyHandlerImpl;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

@API(status = API.Status.STABLE, since = "3.0")
@UtilityClass
public class CometGLFW {
    @Getter
    private Window window;

    public void init(WindowHints windowHints, FullScreenSwitchCombination fullScreenSwitchCombination, boolean debugContext) {
        //CometRenderer doesn't support context swap
        if (CometRenderer.getRegistry().contains(CometTags.INITIALIZED))
            throw new IllegalStateException("CometGLFW initialization must be called before CometRenderer initialization.");

        glfwInit();

        window = new WindowImpl(windowHints, new KeyHandlerImpl(), new KeyHandlerImpl(), fullScreenSwitchCombination, true, debugContext);

        glfwMakeContextCurrent(getWindow().getId());
        GL.createCapabilities();
        setVSync(VsyncMode.Disabled);
    }

    public void setVSync(VsyncMode mode) {
        if (mode == VsyncMode.Adaptive && !isSupportedAdaptiveVsync())
            mode = VsyncMode.Enabled;

        glfwSwapInterval(mode.id);
    }

    public void close() {
        if (window == null)
            throw new IllegalStateException("CometGLFW is already closed.");

        window.close();
        window = null;
    }

    boolean isSupportedAdaptiveVsync() {
        return glfwExtensionSupported("WGL_EXT_swap_control_tear")
                || glfwExtensionSupported("GLX_EXT_swap_control_tear");
    }
}
