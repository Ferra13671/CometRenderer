package com.ferra13671.cometrenderer.sampler;

public interface ISamplerManger {

    ISampler createSampler();

    void bindSampler(int uniformSamplerId, ISampler sampler);
}
