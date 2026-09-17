package com.ferra13671.cometrenderer.plugins.glfw.callback;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
public interface MousePosCallback {

    void onPos(double mouseX, double mouseY);
}
