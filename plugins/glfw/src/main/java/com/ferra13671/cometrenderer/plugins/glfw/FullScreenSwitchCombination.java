package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.plugins.glfw.key.KeyHandler;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

@API(status = API.Status.MAINTAINED, since = "3.0")
@AllArgsConstructor
public enum FullScreenSwitchCombination {
    None(handler -> false),
    F11(handler -> handler.isPressed(GLFW.GLFW_KEY_F11)),
    AltEnter(handler -> (handler.isPressed(GLFW.GLFW_KEY_LEFT_ALT) || handler.isPressed(GLFW.GLFW_KEY_RIGHT_ALT)) && handler.isPressed(GLFW.GLFW_KEY_ENTER)),
    ShiftEnter(handler -> (handler.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || handler.isPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) && handler.isPressed(GLFW.GLFW_KEY_ENTER));

    public final Function<KeyHandler, Boolean> isCombinationPressed;
}
