package com.ferra13671.cometrenderer.device.tracking;

import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@API(status = API.Status.INTERNAL, since = "3.0")
public class ResourceTrackerImpl implements ResourceTracker {
    private final Set<Integer> programs = new HashSet<>();
    private final Set<Integer> shaders = new HashSet<>();
    private final Set<Integer> textures = new HashSet<>();
    private final Set<Integer> framebuffers = new HashSet<>();
    private final Set<Integer> samplers = new HashSet<>();
    private final Set<Integer> vertexArrays = new HashSet<>();

    @Override
    public Set<Integer> getPrograms() {
        return Collections.unmodifiableSet(this.programs);
    }

    @Override
    public Set<Integer> getShaders() {
        return Collections.unmodifiableSet(this.shaders);
    }

    @Override
    public Set<Integer> getTextures() {
        return Collections.unmodifiableSet(this.textures);
    }

    @Override
    public Set<Integer> getFramebuffers() {
        return Collections.unmodifiableSet(this.framebuffers);
    }

    @Override
    public Set<Integer> getSamplers() {
        return Collections.unmodifiableSet(this.samplers);
    }

    @Override
    public Set<Integer> getVertexArrays() {
        return Collections.unmodifiableSet(this.vertexArrays);
    }

    @Override
    public void registerProgram(Integer program) {
        this.programs.add(program);
    }

    @Override
    public void unregisterProgram(Integer program) {
        this.programs.remove(program);
    }

    @Override
    public void registerShader(Integer shader) {
        this.shaders.add(shader);
    }

    @Override
    public void unregisterShader(Integer shader) {
        this.shaders.remove(shader);
    }

    @Override
    public void registerTexture(Integer texture) {
        this.textures.add(texture);
    }

    @Override
    public void unregisterTexture(Integer texture) {
        this.textures.remove(texture);
    }

    @Override
    public void registerFramebuffer(Integer framebuffer) {
        this.framebuffers.add(framebuffer);
    }

    @Override
    public void unregisterFramebuffer(Integer framebuffer) {
        this.framebuffers.remove(framebuffer);
    }

    @Override
    public void registerSampler(Integer sampler) {
        this.samplers.add(sampler);
    }

    @Override
    public void unregisterSampler(Integer sampler) {
        this.samplers.remove(sampler);
    }

    @Override
    public void registerVertexArray(Integer vertexArray) {
        this.vertexArrays.add(vertexArray);
    }

    @Override
    public void unregisterVertexArray(Integer vertexArray) {
        this.vertexArrays.remove(vertexArray);
    }
}
