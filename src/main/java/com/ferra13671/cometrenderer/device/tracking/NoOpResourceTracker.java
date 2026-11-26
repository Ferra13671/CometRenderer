package com.ferra13671.cometrenderer.device.tracking;

import java.util.Set;

public class NoOpResourceTracker implements ResourceTracker {
    public static final NoOpResourceTracker INSTANCE = new NoOpResourceTracker();

    private final Set<Integer> emptySet = Set.of();

    @Override
    public Set<Integer> getPrograms() {
        return this.emptySet;
    }

    @Override
    public Set<Integer> getShaders() {
        return this.emptySet;
    }

    @Override
    public Set<Integer> getTextures() {
        return this.emptySet;
    }

    @Override
    public Set<Integer> getFramebuffers() {
        return this.emptySet;
    }

    @Override
    public Set<Integer> getSamplers() {
        return this.emptySet;
    }

    @Override
    public Set<Integer> getVertexArrays() {
        return this.emptySet;
    }

    @Override
    public void registerProgram(Integer program) {}

    @Override
    public void unregisterProgram(Integer program) {}

    @Override
    public void registerShader(Integer shader) {}

    @Override
    public void unregisterShader(Integer shader) {}

    @Override
    public void registerTexture(Integer texture) {}

    @Override
    public void unregisterTexture(Integer texture) {}

    @Override
    public void registerFramebuffer(Integer framebuffer) {}

    @Override
    public void unregisterFramebuffer(Integer framebuffer) {}

    @Override
    public void registerSampler(Integer sampler) {}

    @Override
    public void unregisterSampler(Integer sampler) {}

    @Override
    public void registerVertexArray(Integer vertexArray) {}

    @Override
    public void unregisterVertexArray(Integer vertexArray) {}
}
