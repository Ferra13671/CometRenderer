package com.ferra13671.cometrenderer.texture;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@AllArgsConstructor
public enum PathMode {
    INSIDE_JAR(path ->
        PathMode.class.getClassLoader().getResourceAsStream(path)
    ),
    OUTSIDE_JAR(path -> {
        try {
            return Files.newInputStream(Paths.get(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });

    public final Function<String, InputStream> streamCreateFunction;
}
