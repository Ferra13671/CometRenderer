package com.ferra13671.cometrenderer.builders;

import java.util.List;

/**
 * Объект, представляющий собой схему программы, используемую при её компиляции в компиляторе.
 *
 * @param name имя программы.
 * @param vertexShader вершинный шейдер.
 * @param fragmentShader фрагментный шейдер.
 * @param uniforms список униформ программы.
 */
public record GlProgramSchema(String name, ShaderSchema vertexShader, ShaderSchema fragmentShader, List<GlUniformSchema<?>> uniforms) {}
