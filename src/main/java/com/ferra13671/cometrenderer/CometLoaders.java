package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.exceptions.LoadShaderContentException;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

public final class CometLoaders {
    public static final CometLoader<String> IN_JAR = new CometLoader<>() {
        @Override
        public String getShaderContent(String path) {
            try {
                InputStream inputStream = CometLoaders.class.getClassLoader().getResourceAsStream(path);
                String content = IOUtils.toString(inputStream);
                inputStream.close();
                return content;
            } catch (Exception e) {
                throw new LoadShaderContentException(e);
            }
        }
    };
    public static final CometLoader<InputStream> INPUT_STREAM = new CometLoader<>() {
        @Override
        public String getShaderContent(InputStream path) {
            try {
                String content = IOUtils.toString(path);
                path.close();
                return content;
            } catch (Exception e) {
                throw new LoadShaderContentException(e);
            }
        }
    };
    public static final CometLoader<URI> URI = new CometLoader<>() {
        @Override
        public String getShaderContent(URI path) {
            try {
                InputStream inputStream = path.toURL().openStream();
                String content = IOUtils.toString(path);
                inputStream.close();
                return content;
            } catch (Exception e) {
                throw new LoadShaderContentException(e);
            }
        }
    };
}
