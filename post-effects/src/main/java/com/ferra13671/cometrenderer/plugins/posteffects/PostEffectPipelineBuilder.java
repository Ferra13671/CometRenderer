package com.ferra13671.cometrenderer.plugins.posteffects;

import java.util.ArrayList;
import java.util.List;

public class PostEffectPipelineBuilder {
    private final List<LocalFrameBufferInfo> localFrameBuffers = new ArrayList<>();
    private final List<ProgramPass> passes = new ArrayList<>();

    public PostEffectPipelineBuilder localFrameBuffers(LocalFrameBufferInfo... frameBuffers) {
        this.localFrameBuffers.addAll(List.of(frameBuffers));
        return this;
    }

    public PostEffectPipelineBuilder pass(ProgramPass programPass) {
        this.passes.add(programPass);
        return this;
    }

    public PostEffectPipeline build() {
        return new PostEffectPipeline(localFrameBuffers, passes);
    }
}
