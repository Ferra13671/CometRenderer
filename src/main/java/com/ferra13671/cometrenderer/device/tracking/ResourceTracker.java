package com.ferra13671.cometrenderer.device.tracking;

import org.apiguardian.api.API;

import java.util.Set;

@API(status = API.Status.INTERNAL, since = "3.1")
public interface ResourceTracker {

    Set<Integer> getPrograms();

    Set<Integer> getShaders();

    Set<Integer> getTextures();

    Set<Integer> getFramebuffers();

    Set<Integer> getSamplers();

    Set<Integer> getVertexArrays();

    void registerProgram(Integer program);

    void unregisterProgram(Integer program);

    void registerShader(Integer shader);

    void unregisterShader(Integer shader);

    void registerTexture(Integer texture);

    void unregisterTexture(Integer texture);

    void registerFramebuffer(Integer framebuffer);

    void unregisterFramebuffer(Integer framebuffer);

    void registerSampler(Integer sampler);

    void unregisterSampler(Integer sampler);

    void registerVertexArray(Integer vertexArray);

    void unregisterVertexArray(Integer vertexArray);
}
