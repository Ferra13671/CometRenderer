package com.ferra13671.cometrenderer;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

public final class CometLoaders {
    public static final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        public String getContent(String path) throws Exception {
            InputStream inputStream = CometLoaders.class.getClassLoader().getResourceAsStream(path);
            String content = IOUtils.toString(inputStream);
            inputStream.close();
            return content;
        }
    };
    public static final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        public String getContent(InputStream path) throws Exception {
            String content = IOUtils.toString(path);
            path.close();
            return content;
        }
    };
    public static final CometLoader<URI> URI = new CometLoader<>() {
        @Override
        public String getContent(URI path) throws Exception {
            InputStream inputStream = path.toURL().openStream();
            String content = IOUtils.toString(path);
            inputStream.close();
            return content;
        }
    };
}
