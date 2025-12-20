package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.gltextureutils.GlTex;
import org.lwjgl.opengl.GL30;

import java.util.function.BiConsumer;

/**
 * Установщик текстуры, принимающий объект текстуры и устанавливающий её в семплер.
 *
 * @param uploadConsumer метод установки текстуры.
 * @param <T> текстура.
 */
public record SamplerUniformUploader<T>(BiConsumer<SamplerUniform, T> uploadConsumer) {

    public static final SamplerUniformUploader<GlTex> GL_TEX = new SamplerUniformUploader<>(
            (samplerUniform, glTex) -> {
                State.TEXTURE.activeTexture(GL30.GL_TEXTURE0 + samplerUniform.getSamplerId());
                State.TEXTURE.bindTexture(glTex.getTexId());
            }
    );
    public static final SamplerUniformUploader<Integer> TEXTURE_ID = new SamplerUniformUploader<>(
            (samplerUniform, id) -> {
                State.TEXTURE.activeTexture(GL30.GL_TEXTURE0 + samplerUniform.getSamplerId());
                State.TEXTURE.bindTexture(id);
            }
    );
}
