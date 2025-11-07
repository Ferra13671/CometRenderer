package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.compile.GlslFileEntry;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

/**
 * Объект, представляющий собой схему шейдера, используемую при его компиляции в компиляторе.
 *
 * @param shaderEntry данные о шейдере.
 * @param shaderType тип шейдера.
 *
 * @see ShaderType
 */
public record ShaderSchema(GlslFileEntry shaderEntry, ShaderType shaderType) {}
