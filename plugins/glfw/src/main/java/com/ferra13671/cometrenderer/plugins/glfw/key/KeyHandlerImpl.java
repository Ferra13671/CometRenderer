package com.ferra13671.cometrenderer.plugins.glfw.key;

import com.ferra13671.cometrenderer.plugins.glfw.callback.KeyCallback;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class KeyHandlerImpl implements KeyHandler {
    private final Set<Integer> activeKeys = new HashSet<>();
    @Setter
    private KeyCallback callback = null;

    @Override
    public void onKey(int key, int action) {
        switch (action) {
            case GLFW.GLFW_PRESS -> {
                this.activeKeys.add(key);

                if (this.callback != null)
                    this.callback.onPress(key);
            }
            case GLFW.GLFW_RELEASE -> {
                this.activeKeys.remove(key);

                if (this.callback != null)
                    this.callback.onRelease(key);
            }
        }
    }

    @Override
    public boolean isPressed(int key) {
        return this.activeKeys.contains(key);
    }
}
