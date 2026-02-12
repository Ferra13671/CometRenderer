package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.lwjgl.opengl.GL13;

import java.util.function.BiConsumer;

public class MinecraftSamplerUniformUploaders {

    public static final BiConsumer<SamplerUniform, AbstractTexture> ABSTRACT_TEXTURE = (samplerUniform, glTexture) -> {
        State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + samplerUniform.getSamplerId());
        State.TEXTURE.bindTexture(glTexture.getId());
    };
}
