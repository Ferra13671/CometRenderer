package com.ferra13671.cometrenderer.sampler.unit;

import com.ferra13671.cometrenderer.CometRenderer;

public record SamplerUnitBindable(int samplerId) implements UnitBindable {
    public static final SamplerUnitBindable EMPTY = new SamplerUnitBindable(0);

    @Override
    public void bind(int unit) {
        CometRenderer.getDevice().getPipelineStateManager().bindSampler(unit, samplerId());
    }
}
