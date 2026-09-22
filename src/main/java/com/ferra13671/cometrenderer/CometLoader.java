package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.shader.GLShaderBuilder;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@API(status = API.Status.MAINTAINED, since = "1.1")
public abstract class CometLoader<T> {
    public static final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        public String load(String path) throws Exception {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public static final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        public String load(InputStream path) throws Exception {
            String content = new BufferedReader(new InputStreamReader(path, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            path.close();
            return content;
        }
    };
    public static final CometLoader<URI> URI = new CometLoader<>() {
        @Override
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

    @API(status = API.Status.EXPERIMENTAL, since = "2.7")
    public GLShaderBuilder<T> createShaderBuilder() {
        return new GLShaderBuilder<>(this);
    }

    @API(status = API.Status.MAINTAINED, since = "1.8.2")
    public GLSLFileEntry createGLSLFileEntry(String name, T path) {
        return new GLSLFileEntry(name, GLSLContent.fromString(getContent(path)), CometCompiler.DEFAULT_GLSL_FILE_ENTRY, new Registry());
    }

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
