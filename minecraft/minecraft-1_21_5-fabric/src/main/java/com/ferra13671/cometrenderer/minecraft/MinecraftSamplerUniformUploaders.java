package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.mojang.blaze3d.opengl.GlTexture;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, GlTexture> GL_TEXTURE = (samplerUniform, glTexture) ->
        CometRenderer.getDevice().getPipelineStateManager().bindTexture(samplerUniform.getSamplerId(), glTexture.glId());
}
