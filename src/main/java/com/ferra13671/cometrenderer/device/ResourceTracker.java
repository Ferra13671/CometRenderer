package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@API(status = API.Status.MAINTAINED, since = "3.0")
public class ResourceTracker {
    private final Set<GLProgram> programs = new HashSet<>();
    private final Set<GLShader> shaders = new HashSet<>();

    public Set<GLProgram> getPrograms() {
        return Collections.unmodifiableSet(this.programs);
    }

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
