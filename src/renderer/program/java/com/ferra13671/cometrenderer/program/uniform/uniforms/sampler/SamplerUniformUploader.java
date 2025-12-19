package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.gltextureutils.GlTex;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.GlTextureView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
                GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + samplerUniform.getSamplerId());
                GlStateManager._bindTexture(glTex.getTexId());
            }
    );
    public static final SamplerUniformUploader<GlTextureView> GL_TEXTURE_VIEW = new SamplerUniformUploader<>(
            (samplerUniform, textureView) -> {
                GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + samplerUniform.getSamplerId());
                GlTexture glTexture = textureView.texture();
                int o;
                if ((glTexture.usage() & 16) != 0) {
                    o = 34067;
                    GL11.glBindTexture(34067, glTexture.getGlId());
                } else {
                    o = GlConst.GL_TEXTURE_2D;
                    GlStateManager._bindTexture(glTexture.getGlId());
                }

                GlStateManager._texParameter(o, 33084, textureView.baseMipLevel());
                GlStateManager._texParameter(o, GL12.GL_TEXTURE_MAX_LEVEL, textureView.baseMipLevel() + textureView.mipLevels() - 1);
                glTexture.checkDirty(o);
            }
    );
    public static final SamplerUniformUploader<Integer> TEXTURE_ID = new SamplerUniformUploader<>(
            (samplerUniform, id) -> {
                GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + samplerUniform.getSamplerId());
                GlStateManager._bindTexture(id);
            }
    );
}
