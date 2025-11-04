package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.TextureUtils.GlTex;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.GlTextureView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.function.BiConsumer;

public record SamplerUniformApplier<T>(BiConsumer<SamplerUniform, T> applyConsumer) {

    public static final SamplerUniformApplier<GlTex> GL_TEX = new SamplerUniformApplier<>(
            (samplerUniform, glTex) -> {
                GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + samplerUniform.getSamplerId());
                GlStateManager._bindTexture(glTex.getTexId());
            }
    );
    public static final SamplerUniformApplier<GlTextureView> GL_TEXTURE_VIEW = new SamplerUniformApplier<>(
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
    public static final SamplerUniformApplier<Integer> TEXTURE_ID = new SamplerUniformApplier<>(
            (samplerUniform, id) -> {
                GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + samplerUniform.getSamplerId());
                GlStateManager._bindTexture(id);
            }
    );
}
