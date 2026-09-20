package com.ferra13671.cometrenderer.device;

import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@API(status = API.Status.INTERNAL, since = "3.0")
class ResourceTracker {
    private final Set<Integer> programs = new HashSet<>();
    private final Set<Integer> shaders = new HashSet<>();
    private final Set<Integer> framebuffers = new HashSet<>();
    private final Set<Integer> samplers = new HashSet<>();
    private final Set<Integer> vertexArrays = new HashSet<>();

    Set<Integer> getPrograms() {
        return Collections.unmodifiableSet(this.programs);
    }

    Set<Integer> getShaders() {
        return Collections.unmodifiableSet(this.shaders);
    }

    Set<Integer> getFramebuffers() {
        return Collections.unmodifiableSet(this.framebuffers);
    }

    Set<Integer> getSamplers() {
        return Collections.unmodifiableSet(this.samplers);
    }

    Set<Integer> getVertexArrays() {
        return Collections.unmodifiableSet(this.vertexArrays);
    }

    void registerProgram(Integer program) {
        this.programs.add(program);
    }

    void unregisterProgram(Integer program) {
        this.programs.remove(program);
    }

    void registerShader(Integer shader) {
        this.shaders.add(shader);
    }

    void unregisterShader(Integer shader) {
        this.shaders.remove(shader);
    }

    void registerFramebuffer(Integer framebuffer) {
        this.framebuffers.add(framebuffer);
    }

    void unregisterFramebuffer(Integer framebuffer) {
        this.framebuffers.remove(framebuffer);
    }

    void registerSampler(Integer sampler) {
        this.samplers.add(sampler);
    }

    void unregisterSampler(Integer sampler) {
        this.samplers.remove(sampler);
    }

    void registerVertexArray(Integer vertexArray) {
        this.vertexArrays.add(vertexArray);
    }

    void unregisterVertexArray(Integer vertexArray) {
        this.vertexArrays.remove(vertexArray);
    }
}
