package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
@AllArgsConstructor
public enum VsyncMode {
    Disabled(0),
    Enabled(1),
    Adaptive(-1);

    public final int id;
}
