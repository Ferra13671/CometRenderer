package com.ferra13671.cometrenderer.program.schema;

import com.ferra13671.cometrenderer.program.shader.ShaderType;

/*
 * Схема, используемая при компиляции шейдера.
 */
public record ShaderSchema(String name, String shaderId, ShaderType shaderType) {}
