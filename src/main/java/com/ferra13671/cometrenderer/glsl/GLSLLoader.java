package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.ErrorHandlers;
import org.apiguardian.api.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@API(status = API.Status.MAINTAINED, since = "1.1")
public abstract class GLSLLoader<T> {
    public static final GLSLLoader<String> IN_JAR = new GLSLLoader<>() {
        @Override
        public String load(String path) throws Exception {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public static final GLSLLoader<InputStream> INPUT_STREAM = new GLSLLoader<>() {
        @Override
        public String load(InputStream path) throws Exception {
            String content = new BufferedReader(new InputStreamReader(path, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            path.close();
            return content;
        }
    };
    public static final GLSLLoader<URI> URI = new GLSLLoader<>() {
        @Override
        public String load(URI path) throws Exception {
            InputStream inputStream = path.toURL().openStream();
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public static final GLSLLoader<String> STRING = new GLSLLoader<>() {
        @Override
        public String load(String path) {
            return path;
        }
    };

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public String getContent(T path) {
        String content = null;
        try {
            content = load(path);
        } catch (Exception e) {
            ErrorHandlers.onLoadGLSLContentException(e);
        }
        return content;
    }

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public abstract String load(T path) throws Exception;
}
