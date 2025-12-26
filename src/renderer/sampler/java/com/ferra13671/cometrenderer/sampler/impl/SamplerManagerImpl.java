package com.ferra13671.cometrenderer.sampler.impl;

import com.ferra13671.cometrenderer.sampler.ISampler;
import com.ferra13671.cometrenderer.sampler.ISamplerManger;
import org.lwjgl.opengl.GL33;

public class SamplerManagerImpl implements ISamplerManger {

    @Override
    public ISampler createSampler() {
        return new SamplerImpl();
    }

    @Override
    public void bindSampler(int unit, ISampler sampler) {
        if (sampler == null)
            GL33.glBindSampler(unit, 0);
        else
            GL33.glBindSampler(unit, ((SamplerImpl) sampler).getId());
    }
}
