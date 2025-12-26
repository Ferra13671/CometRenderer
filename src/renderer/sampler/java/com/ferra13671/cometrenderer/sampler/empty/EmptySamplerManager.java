package com.ferra13671.cometrenderer.sampler.empty;

import com.ferra13671.cometrenderer.sampler.ISampler;
import com.ferra13671.cometrenderer.sampler.ISamplerManger;

public class EmptySamplerManager implements ISamplerManger {

    @Override
    public ISampler createSampler() {
        return new EmptySampler();
    }

    @Override
    public void bindSampler(int uniformSamplerId, ISampler sampler) {
    }
}
