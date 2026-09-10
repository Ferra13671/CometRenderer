package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.device.directstate.ARBDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DefaultDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.device.vertexformat.ARBVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.DefaultVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.VertexFormatManager;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Setter
@API(status = API.Status.MAINTAINED, since = "2.9")
public class GLDevice {
    @Getter
    private final DirectStateManager directStateManager;
    @Getter
    private final VertexFormatManager vertexFormatManager;
    @Getter
    private final MeshBufferManager meshBufferManager;

    @API(status = API.Status.MAINTAINED, since = "3.0")
    private final Set<GLProgram> programs = new HashSet<>();
    @API(status = API.Status.MAINTAINED, since = "3.0")
    private final Set<GLShader> shaders = new HashSet<>();

    @API(status = API.Status.MAINTAINED, since = "3.0")
    @Getter
    private GLProgram currentProgram;

    public GLDevice() {
        this.directStateManager = GLCapabilities.supportsDirectStateAccess() ?
                new ARBDirectStateManager()
                :
                new DefaultDirectStateManager();

        this.vertexFormatManager = GLCapabilities.supportsVertexAttributeBindings() ?
                new ARBVertexFormatManager()
                :
                new DefaultVertexFormatManager();

        this.meshBufferManager = GLCapabilities.supportsBufferStorage() ?
                MeshBufferManager.ARB
                :
                MeshBufferManager.DEFAULT;
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public Set<GLProgram> getPrograms() {
        return Collections.unmodifiableSet(this.programs);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public Set<GLShader> getShaders() {
        return Collections.unmodifiableSet(this.shaders);
    }

    @API(status = API.Status.INTERNAL)
    public void registerProgram(GLProgram program) {
        this.programs.add(program);
    }

    @API(status = API.Status.INTERNAL)
    public void unregisterProgram(GLProgram program) {
        this.programs.remove(program);
    }

    @API(status = API.Status.INTERNAL)
    public void registerShader(GLShader shader) {
        this.shaders.add(shader);
    }

    @API(status = API.Status.INTERNAL)
    public void unregisterShader(GLShader shader) {
        this.shaders.remove(shader);
    }
}
