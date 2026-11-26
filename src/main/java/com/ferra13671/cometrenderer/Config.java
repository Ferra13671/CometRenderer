package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.MathUtils;
import lombok.Builder;
import lombok.Getter;
import org.apiguardian.api.API;

import java.awt.*;

@API(status = API.Status.MAINTAINED, since = "1.9")
@Getter
@Builder
public class Config {
    @Builder.Default
    private boolean checkOpenGLVersion = true;
    @Builder.Default
    private GLVersion minimumOpenGLVersion = GLVersion.GL33;
    @Builder.Default
    private int defaultMeshAllocatorSize = 786432;
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private int allocatorPoolSize = 32;
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private boolean trackingResources = true;
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private boolean logShaderSources = false;
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private Color defaultClearColor = new Color(255, 255, 255, 0);
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private double defaultClearDepth = 1;
    @API(status = API.Status.MAINTAINED, since = "3.1")
    @Builder.Default
    private int defaultClearStencil = 0;

    public static class ConfigBuilder {
        public ConfigBuilder minimumOpenGLVersion(GLVersion version) {
            if (version.id < GLVersion.GL33.id) {
                ErrorHandlers.onSetUnsupportedOpenGLVersion(version, GLVersion.GL33);
            } else {
                this.minimumOpenGLVersion$value = version;
                this.minimumOpenGLVersion$set = true;
            }
            return this;
        }

        public ConfigBuilder defaultMeshAllocatorSize(int defaultMeshAllocatorSize) {
            this.defaultMeshAllocatorSize$value = MathUtils.clamp(defaultMeshAllocatorSize, 0, Integer.MAX_VALUE);
            this.defaultMeshAllocatorSize$set = true;
            return this;
        }

        public ConfigBuilder allocatorPoolSize(int allocatorPoolSize) {
            this.allocatorPoolSize$value = MathUtils.clamp(allocatorPoolSize, 1, 256);
            this.allocatorPoolSize$set = true;
            return this;
        }
    }
}