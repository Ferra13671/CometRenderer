package com.ferra13671.cometrenderer.plugins.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

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
}
