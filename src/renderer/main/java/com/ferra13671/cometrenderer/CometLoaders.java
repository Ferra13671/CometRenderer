package com.ferra13671.cometrenderer;

import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Самые основные загрузчики GLSL контента.
 *
 * @see CometLoader
 */
@API(status = API.Status.MAINTAINED, since = "1.1")
@UtilityClass
public class CometLoaders {
    public final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        public String load(String path) throws Exception {
            InputStream inputStream = CometLoaders.class.getClassLoader().getResourceAsStream(path);
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        public String load(InputStream path) throws Exception {
            String content = new BufferedReader(new InputStreamReader(path, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            path.close();
            return content;
        }
    };
    public final CometLoader<URI> URI = new CometLoader<>() {
        @Override
        public String load(URI path) throws Exception {
            InputStream inputStream = path.toURL().openStream();
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    public final CometLoader<String> STRING = new CometLoader<>() {
        @Override
        public String load(String path) {
            return path;
        }
    };
}
