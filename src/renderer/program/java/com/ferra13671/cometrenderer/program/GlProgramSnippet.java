package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.builders.GlProgramBuilder;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;
import com.ferra13671.cometrenderer.compile.GlslFileEntry;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

import java.util.HashMap;
import java.util.List;

/**
 * Фрагмент программы, который может быть добавлен любой программе.
 * Позволяет более удобным способом добавлять в множество программ одну и ту же информацию.
 *
 * @param shaders карта шейдеров фрагмента программы по их типу.
 * @param uniforms список юниформ фрагмента программы.
 *
 * @see GlProgram
 * @see GlProgramSnippet
 */
public record GlProgramSnippet(HashMap<ShaderType, GlslFileEntry> shaders, List<GlUniformSchema<?>> uniforms) {

    /**
     * Применяет фрагмент программы к сборщику программы.
     *
     * @param builder сборщик программы.
     * @param <T> тип объекта, используемого как путь к контенту шейдеров.
     */
    public <T> void applyTo(GlProgramBuilder<T> builder) {
        shaders.forEach((type, glslFileEntry) -> builder.shader(glslFileEntry, type));
        uniforms.forEach(builder::uniform);
    }
}