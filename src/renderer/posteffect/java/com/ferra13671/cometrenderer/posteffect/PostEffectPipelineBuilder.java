package com.ferra13671.cometrenderer.posteffect;

import java.util.ArrayList;
import java.util.List;

/*
 * Билдер пост эффекта
 */
public class PostEffectPipelineBuilder {
    private final List<LocalFrameBufferInfo> localFrameBuffers = new ArrayList<>();
    private final List<PostEffectPipelineAction> actions = new ArrayList<>();

    /*
     * Добавляет локальные фреймбуфферы в пост эффект
     */
    public PostEffectPipelineBuilder localFrameBuffers(LocalFrameBufferInfo... frameBuffers) {
        this.localFrameBuffers.addAll(List.of(frameBuffers));
        return this;
    }

    /*
     * Добавляет "под эффект" пост эффекту
     */
    public PostEffectPipelineBuilder pass(ProgramPass programPass) {
        this.actions.add(programPass);
        return this;
    }

    /*
     * Билдит пост эффект
     */
    public PostEffectPipeline build() {
        return new PostEffectPipeline(localFrameBuffers, actions);
    }
}
