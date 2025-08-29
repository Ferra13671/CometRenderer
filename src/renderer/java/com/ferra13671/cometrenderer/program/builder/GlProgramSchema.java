package com.ferra13671.cometrenderer.program.builder;

import java.util.List;

/*
 * Схема, используемая при компиляции программы.
 */
public record GlProgramSchema<T>(String name, ShaderSchema<T> vertexShader, ShaderSchema<T> fragmentShader, List<GlUniformSchema> uniforms) {}
