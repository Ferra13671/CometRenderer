package com.ferra13671.cometrenderer.program.builder;

import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

/*
 * Схема, используемая при компиляции шейдера.
 */
public record ShaderSchema(GlslFileEntry shaderEntry, ShaderType shaderType) {}
