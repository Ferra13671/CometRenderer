package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.GLVersion;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.9")
public class Config {
    @Getter
    @Setter
    private boolean checkOpenGLVersion = true;
    @Getter
    private GLVersion minimumOpenGLVersion = GLVersion.GL33;
    @Getter
    @Setter
    private boolean compareCurrentAndShaderOpenGLVersions = true;
    @Getter
    private int defaultMeshAllocatorSize = 786432;

    public void setMinimumOpenGLVersion(GLVersion version) {
        if (version.id < GLVersion.GL33.id)
            new IllegalStateException("Cannot set minimum OpenGL version lower than 3.3").printStackTrace(System.err);

        this.minimumOpenGLVersion = version;
    }

    public void setDefaultMeshAllocatorSize(int defaultMeshAllocatorSize) {
        this.defaultMeshAllocatorSize = Math.clamp(defaultMeshAllocatorSize, 0, Integer.MAX_VALUE);
    }
}
