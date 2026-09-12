package com.ferra13671.cometrenderer.sampler.unit;

import com.ferra13671.cometrenderer.CometRenderer;

public record TextureUnitBindable(int textureId) implements UnitBindable {
    public static final TextureUnitBindable EMPTY = new TextureUnitBindable(0);

    @Override
    public void bind(int unit) {
        CometRenderer.getDevice().getPipelineStateManager().bindTexture(unit, textureId());
    }
}
