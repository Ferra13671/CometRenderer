package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.program.schema.GlProgramBuilder;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippet;

/*
 * Лоадер шейдеров
 */
public abstract class CometLoader<T> {

    /*
     * Создает новый билдер программы, использующий данный лоадер
     */
    public GlProgramBuilder<T> createBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(this::getShaderContent, snippets);
    }

    /*
     * Возвращает контент шейдера при помощи пути
     */
    public abstract String getShaderContent(T path);
}
