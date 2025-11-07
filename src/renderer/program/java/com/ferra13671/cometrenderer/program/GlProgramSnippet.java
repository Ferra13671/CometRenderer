package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.builders.GlProgramSnippetBuilder;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/**
 * Фрагмент программы, который может быть добавлен любой программе.
 * Позволяет более удобным способом добавлять в множество программ одну и ту же информацию.
 *
 * @param uniforms список юниформ фрагмента программы.
 *
 * @see GlProgram
 */
//TODO больше информации, которая может быть добавлена фрагментом программы
public record GlProgramSnippet(List<GlUniformSchema<?>> uniforms) {

    /**
     * Возвращает новый сборщик фрагмента программы.
     *
     * @return новый сборщик фрагмента программы.
     * @see com.ferra13671.cometrenderer.builders.GlProgramSnippetBuilder
     */
    public static GlProgramSnippetBuilder builder() {
        return new GlProgramSnippetBuilder();
    }
}