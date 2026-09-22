package com.ferra13671.cometrenderer.texture.loader;

import com.ferra13671.cometrenderer.texture.PathMode;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public record FileEntry(String path, PathMode pathMode) {
}
