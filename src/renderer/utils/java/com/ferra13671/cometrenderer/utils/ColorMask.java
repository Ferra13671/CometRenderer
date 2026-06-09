package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "2.9")
public record ColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
}
