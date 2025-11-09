package com.ferra13671.cometrenderer.compile;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.HashMap;

/**
 * Шейдерная библиотека позволяет уменьшить размер загружаемого контента множество шейдеров, использующих один и тот же код.
 * Библиотека по своему строению не отличается от обычного glsl контента, за исключением того, что она дополнительно хранит список униформ, которая она будет добавлять в шейдер и программу соответственно.
 * Все шейдерные библиотеки добавляются в шейдеры на этапе компиляции в компиляторе, поэтому шейдерные библиотеки нужно загружать до начала компиляции шейдеров, однако можно загружать во время загрузки glsl контента шейдера.
 *
 * @param libraryEntry glsl контент библиотеки.
 * @param uniforms карта униформ, которая будет добавлять библиотека в шейдер и программу соответственно.
 *
 * @see GlobalCometCompiler
 * @see CometLoader
 */
public record GlShaderLibrary(GlslFileEntry libraryEntry, HashMap<String, UniformType<?>> uniforms) {
}
