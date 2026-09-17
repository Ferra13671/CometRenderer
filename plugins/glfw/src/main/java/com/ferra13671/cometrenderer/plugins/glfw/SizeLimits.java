package com.ferra13671.cometrenderer.plugins.glfw;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
public record SizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
}
