package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.mojang.blaze3d.opengl.GlTexture;
import org.lwjgl.opengl.GL13;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, GlTexture> GL_TEXTURE = (samplerUniform, glTexture) -> {
        State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + samplerUniform.getSamplerId());
        State.TEXTURE.bindTexture(glTexture.glId());
    };
}
