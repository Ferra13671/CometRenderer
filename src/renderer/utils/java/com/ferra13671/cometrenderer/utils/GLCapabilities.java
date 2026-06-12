package com.ferra13671.cometrenderer.utils;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL;

@UtilityClass
@API(status = API.Status.INTERNAL, since = "2.9")
public class GLCapabilities {

    public boolean supportsShader(ShaderType shaderType) {
        return supportsVersion(shaderType.glVersion);
    }

    public boolean supportsSamplerObjects() {
        return supportsVersion(GLVersion.GL33) || GL.getCapabilities().GL_ARB_sampler_objects;
    }

    public boolean supportsVertexAttributeBindings() {
        return GL.getCapabilities().GL_ARB_vertex_attrib_binding;
    }

    public boolean supportsVersion(GLVersion glVersion) {
        return CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().id >= glVersion.id;
    }
}
