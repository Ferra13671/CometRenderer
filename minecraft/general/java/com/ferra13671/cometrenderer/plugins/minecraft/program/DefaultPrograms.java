package com.ferra13671.cometrenderer.plugins.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;

public class DefaultPrograms {
    public final GlProgram POSITION = CometLoaders.STRING.createProgramBuilder()
            .name("position")
            .shader(DefaultShaderEntries.POSITION_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.POSITION_FRAGMENT, ShaderType.Fragment)
            .build();

    public final GlProgram POSITION_COLOR = CometLoaders.STRING.createProgramBuilder()
            .name("position-color")
            .shader(DefaultShaderEntries.POSITION_COLOR_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.POSITION_COLOR_FRAGMENT, ShaderType.Fragment)
            .build();

    public final GlProgram POSITION_TEXTURE = CometLoaders.STRING.createProgramBuilder()
            .name("position-texture")
            .shader(DefaultShaderEntries.POSITION_TEXTURE_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.POSITION_TEXTURE_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .build();

    public final GlProgram POSITION_TEXTURE_COLOR = CometLoaders.STRING.createProgramBuilder()
            .name("position-texture-color")
            .shader(DefaultShaderEntries.POSITION_TEXTURE_COLOR_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.POSITION_TEXTURE_COLOR_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .build();

    public final GlProgram ROUNDED_RECT = CometLoaders.STRING.createProgramBuilder()
            .name("rounded-rect")
            .shader(DefaultShaderEntries.ROUNDED_RECT_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.ROUNDED_RECT_FRAGMENT, ShaderType.Fragment)
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram ROUNDED_TEXTURE = CometLoaders.STRING.createProgramBuilder()
            .name("rounded-texture")
            .shader(DefaultShaderEntries.ROUNDED_TEXTURE_VERTEX, ShaderType.Vertex)
            .shader(DefaultShaderEntries.ROUNDED_TEXTURE_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .uniform("height", UniformType.FLOAT)
            .build();
}
