package com.ferra13671.cometrenderer.texture.loader;

import org.apiguardian.api.API;

import java.io.InputStream;
import java.net.URI;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
public interface GifLoader<T> {
    GifLoader<InputStream> INPUT_STREAM = path -> path;
    GifLoader<URI> URI = path -> path.toURL().openStream();
    GifLoader<String> IN_JAR = path -> GifLoader.class.getClassLoader().getResourceAsStream(path);

    InputStream load(T path) throws Exception;
}
