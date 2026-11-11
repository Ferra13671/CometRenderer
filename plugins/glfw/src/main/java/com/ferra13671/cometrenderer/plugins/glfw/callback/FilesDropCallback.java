package com.ferra13671.cometrenderer.plugins.glfw.callback;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
public interface FilesDropCallback {

    void onFilesDropCallback(String[] paths);
}
