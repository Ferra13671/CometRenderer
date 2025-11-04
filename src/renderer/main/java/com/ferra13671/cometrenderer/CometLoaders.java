package com.ferra13671.cometrenderer;

import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class CometLoaders {
    public static final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String getContent(String path) throws Exception {
            InputStream inputStream = CometLoaders.class.getClassLoader().getResourceAsStream(path);
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            return content;
        }
    };
    public static final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String getContent(InputStream path) throws Exception {
            String content = IOUtils.toString(path, StandardCharsets.UTF_8);
            path.close();
            return content;
        }
    };
    public static final CometLoader<URI> URI = new CometLoader<>() {
        @Override
        @OverriddenMethod
        public String getContent(URI path) throws Exception {
            InputStream inputStream = path.toURL().openStream();
            String content = IOUtils.toString(path, StandardCharsets.UTF_8);
            inputStream.close();
            return content;
        }
    };
}
