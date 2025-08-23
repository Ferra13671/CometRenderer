package com.ferra13671.cometrenderer.program.schema;

import com.ferra13671.cometrenderer.program.shader.ShaderType;

import java.util.function.Function;

/*
 * Схема, используемая при компиляции шейдера.
 */
public record ShaderSchema<T>(String name, Function<T, String> contentGetter, T shaderPath, ShaderType shaderType) {}
