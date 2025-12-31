package com.ferra13671.cometrenderer;

import com.ferra13671.ferraguard.annotations.OverriddenMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Самые основные загрузчики glsl контента.
 *
 * @see CometLoader
 */
public final class CometLoaders {
    public static final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String load(String path) throws Exception {
            InputStream inputStream = CometLoaders.class.getClassLoader().getResourceAsStream(path);
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public static final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String load(InputStream path) throws Exception {
            String content = new BufferedReader(new InputStreamReader(path, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            path.close();
            return content;
        }
    };
    public static final CometLoader<URI> URI = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String load(URI path) throws Exception {
            InputStream inputStream = path.toURL().openStream();
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public static final CometLoader<String> STRING = new CometLoader<>() {
        @Override
        public String load(String path) {
            return path;
        }
    };
}
