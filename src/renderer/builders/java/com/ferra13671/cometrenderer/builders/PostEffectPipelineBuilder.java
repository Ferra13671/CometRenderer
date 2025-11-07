package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.posteffect.LocalFrameBufferInfo;
import com.ferra13671.cometrenderer.posteffect.PostEffectPipeline;
import com.ferra13671.cometrenderer.posteffect.ProgramPass;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик пост эффекта.
 *
 * @see PostEffectPipeline
 */
public class PostEffectPipelineBuilder {
    /** Список информации о локальных фреймбуфферах. **/
    private final List<LocalFrameBufferInfo> localFrameBuffers = new ArrayList<>();
    /** Список всех пассов пост эффекта. **/
    private final List<ProgramPass> passes = new ArrayList<>();

    /**
     * Добавляет локальные фреймбуфферы в пост эффект.
     *
     * @param frameBuffers локальные фреймбуфферы.
     * @return сборщик пост эффекта.
     *
     * @see LocalFrameBufferInfo
     */
    public PostEffectPipelineBuilder localFrameBuffers(LocalFrameBufferInfo... frameBuffers) {
        this.localFrameBuffers.addAll(List.of(frameBuffers));
        return this;
    }

    /**
     * Добавляет пасс в пост эффект.
     *
     * @param programPass пасс.
     * @return сборщик пост эффекта.
     *
     * @see ProgramPass
     */
    public PostEffectPipelineBuilder pass(ProgramPass programPass) {
        this.passes.add(programPass);
        return this;
    }

    /**
     * Собирает данные в новый пост эффект.
     *
     * @return новый пост эффект.
     *
     * @see PostEffectPipeline
     */
    public PostEffectPipeline build() {
        return new PostEffectPipeline(localFrameBuffers, passes);
    }
}
