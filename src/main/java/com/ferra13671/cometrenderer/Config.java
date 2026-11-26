package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.MathUtils;
import lombok.Builder;
import lombok.Getter;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.9")
@Getter
@Builder
public class Config {
    @Builder.Default
    private boolean checkOpenGLVersion = true;
    @Builder.Default
    private GLVersion minimumOpenGLVersion = GLVersion.GL33;
    @Builder.Default
    private boolean compareCurrentAndShaderOpenGLVersions = true;
    @Builder.Default
    private int defaultMeshAllocatorSize = 786432;
    @Builder.Default
    private int allocatorPoolSize = 32;
    @Builder.Default
    private boolean trackingResources = true;

    public static class ConfigBuilder {
        public ConfigBuilder minimumOpenGLVersion(GLVersion version) {
            if (version.id < GLVersion.GL33.id) {
                new IllegalStateException("Cannot set minimum OpenGL version lower than 3.3").printStackTrace(System.err);
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