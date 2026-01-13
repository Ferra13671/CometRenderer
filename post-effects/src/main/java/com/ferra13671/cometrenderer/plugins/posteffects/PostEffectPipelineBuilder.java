package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.utils.Builder;

import java.util.ArrayList;
import java.util.List;

public class PostEffectPipelineBuilder extends Builder<PostEffectPipeline> {
    private final List<LocalFrameBufferInfo> localFrameBuffers = new ArrayList<>();
    private final List<ProgramPass> passes = new ArrayList<>();

    public PostEffectPipelineBuilder() {
        super("post-effect pipeline");
    }

    public PostEffectPipelineBuilder localFrameBuffers(LocalFrameBufferInfo... frameBuffers) {
        this.localFrameBuffers.addAll(List.of(frameBuffers));
        return this;
    }

    public PostEffectPipelineBuilder pass(ProgramPass programPass) {
        this.passes.add(programPass);
        return this;
    }

    @Override
    public PostEffectPipeline build() {
        return new PostEffectPipeline(this.localFrameBuffers, this.passes);
    }
}
