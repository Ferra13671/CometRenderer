package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.GlTextureView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, GlTextureView> GL_TEXTURE_VIEW = (samplerUniform, textureView) -> {
        State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + samplerUniform.getSamplerId());
        GlTexture glTexture = textureView.texture();
        int o;
        if ((glTexture.usage() & 16) != 0) {
            o = 34067;
            GL11.glBindTexture(o, glTexture.getGlId());
        } else {
            o = GlConst.GL_TEXTURE_2D;
            State.TEXTURE.bindTexture(glTexture.getGlId());
        }

        GlStateManager._texParameter(o, 33084, textureView.baseMipLevel());
        GlStateManager._texParameter(o, GL12.GL_TEXTURE_MAX_LEVEL, textureView.baseMipLevel() + textureView.mipLevels() - 1);
    };
    public static final BiConsumer<SamplerUniform, GlTexture> GL_TEXTURE = (samplerUniform, glTexture) -> {
        State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + samplerUniform.getSamplerId());
        int o;
        if ((glTexture.usage() & 16) != 0) {
            o = 34067;
            GL11.glBindTexture(o, glTexture.getGlId());
        } else
            State.TEXTURE.bindTexture(glTexture.getGlId());
    };
}
