package com.ferra13671.cometrenderer.plugins.glfw.key;

import com.ferra13671.cometrenderer.plugins.glfw.callback.KeyCallback;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
public interface KeyHandler {

    void onKey(int key, int action);

    boolean isPressed(int key);

    void setCallback(KeyCallback callback);
}
