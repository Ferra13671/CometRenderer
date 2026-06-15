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
        return shaderType.supportConsumer.get();
    }

    public boolean supportsTesselationShader() {
        return supportsVersion(GLVersion.GL40) || GL.getCapabilities().GL_ARB_tessellation_shader;
    }

    public boolean supportsComputeShader() {
        return supportsVersion(GLVersion.GL43) || GL.getCapabilities().GL_ARB_compute_shader;
    }

    public boolean supportsVertexAttributeBindings() {
        return GL.getCapabilities().GL_ARB_vertex_attrib_binding;
    }

    public boolean supportsDirectStateAccess() {
        return GL.getCapabilities().GL_ARB_direct_state_access;
    }

    public boolean supportsVersion(GLVersion glVersion) {
        return CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().id >= glVersion.id;
    }
}
