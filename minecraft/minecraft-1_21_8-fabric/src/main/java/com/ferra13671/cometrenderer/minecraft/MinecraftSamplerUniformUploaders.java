package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.opengl.GlTextureView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, GlTextureView> GL_TEXTURE_VIEW = (samplerUniform, textureView) -> {
        GlTexture glTexture = textureView.texture();
        if ((glTexture.usage() & 16) != 0) {
            CometRenderer.getDevice().getPipelineStateManager().ensureTextureUnit(samplerUniform.getSamplerId());
            GL11.glBindTexture(34067, glTexture.glId());
        } else {
            CometRenderer.getDevice().getPipelineStateManager().bindTexture(samplerUniform.getSamplerId(), glTexture.glId());
        }

        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, 33084, textureView.baseMipLevel());
        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, textureView.baseMipLevel() + textureView.mipLevels() - 1);
    };
    public static final BiConsumer<SamplerUniform, GlTexture> GL_TEXTURE = (samplerUniform, glTexture) -> {
        if ((glTexture.usage() & 16) != 0) {
            CometRenderer.getDevice().getPipelineStateManager().ensureTextureUnit(samplerUniform.getSamplerId());
            GL11.glBindTexture(34067, glTexture.glId());
        } else
            CometRenderer.getDevice().getPipelineStateManager().bindTexture(samplerUniform.getSamplerId(), glTexture.glId());
    };
}
