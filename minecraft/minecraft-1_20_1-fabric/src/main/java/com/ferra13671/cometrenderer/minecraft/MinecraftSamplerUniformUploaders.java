package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import net.minecraft.client.renderer.texture.AbstractTexture;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, AbstractTexture> ABSTRACT_TEXTURE = (samplerUniform, glTexture) ->
        CometRenderer.getDevice().getPipelineStateManager().bindTexture(samplerUniform.getSamplerId(), glTexture.getId());
}
