package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.GLVersion;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.gltextureutils.GlTex;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.util.function.BiConsumer;

/**
 * Установщик текстуры, принимающий объект текстуры и устанавливающий её в семплер.
 *
 * @param uploadConsumer метод установки текстуры.
 * @param <T> текстура.
 */
//TODO мб переделать
public record SamplerUniformUploader<T>(BiConsumer<SamplerUniform, T> uploadConsumer) {

    public static final SamplerUniformUploader<GlTex> GL_TEX = CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().id >= GLVersion.GL33.id ?
            new SamplerUniformUploader<>(
                    (samplerUniform, glTex) -> {
                        int samplerId = GL13.GL_TEXTURE0 + samplerUniform.getSamplerId();

                        State.TEXTURE.activeTexture(samplerId);
                        State.TEXTURE.bindTexture(glTex.getTexId());

                        GL33.glSamplerParameteri(samplerId, GL11.GL_TEXTURE_MIN_FILTER, glTex.getFiltering().id);
                        GL33.glSamplerParameteri(samplerId, GL11.GL_TEXTURE_MAG_FILTER, glTex.getFiltering().id);
                    }
            )
            :
            new SamplerUniformUploader<>(
                    (samplerUniform, glTex) -> {
                        State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + samplerUniform.getSamplerId());
                        State.TEXTURE.bindTexture(glTex.getTexId());
                    }
            );
    public static final SamplerUniformUploader<Integer> TEXTURE_ID = CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().id >= GLVersion.GL33.id ?
            new SamplerUniformUploader<>(
                    (samplerUniform, id) -> {
                        int samplerId = GL13.GL_TEXTURE0 + samplerUniform.getSamplerId();

                        State.TEXTURE.activeTexture(samplerId);
                        State.TEXTURE.bindTexture(id);

                        GL33.glSamplerParameteri(samplerId, GL11.GL_TEXTURE_MIN_FILTER, GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER));
                        GL33.glSamplerParameteri(samplerId, GL11.GL_TEXTURE_MAG_FILTER, GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER));
                    }
            )
            :
            new SamplerUniformUploader<>(
                    (samplerUniform, id) -> {
                        State.TEXTURE.activeTexture(GL30.GL_TEXTURE0 + samplerUniform.getSamplerId());
                        State.TEXTURE.bindTexture(id);
                    }
            );
}
