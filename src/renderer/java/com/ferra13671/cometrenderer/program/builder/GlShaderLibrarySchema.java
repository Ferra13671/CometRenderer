package com.ferra13671.cometrenderer.program.builder;

import java.util.List;
import java.util.function.Function;

/*
 * Схема, используемая при компиляции шейдерной библиотеки
 */
public record GlShaderLibrarySchema<T>(String name, Function<T, String> contentGetter, T shaderPath, List<GlUniformSchema> uniforms) {
}
