package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.exceptions.impl.LoadLibraryContentException;
import com.ferra13671.cometrenderer.exceptions.impl.LoadShaderContentException;
import com.ferra13671.cometrenderer.program.builder.GlProgramBuilder;
import com.ferra13671.cometrenderer.program.builder.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippet;

/*
 * Лоадер шейдеров и библиотек
 */
public abstract class CometLoader<T> {

    /*
     * Создает новый билдер программы, использующий данный лоадер
     */
    public GlProgramBuilder<T> createProgramBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(path -> {
            String content = null;
            try {
                content = getContent(path);
            } catch (Exception e) {
                ExceptionPrinter.printAndExit(new LoadShaderContentException(e));
            }
            return content;
        }, snippets);
    }

    /*
     * Создает новый билдер шейдерной библиотеки, использующий данный лоадер
     */
    public GlShaderLibraryBuilder<T> createLibraryBuilder(GlProgramSnippet... snippets) {
        return new GlShaderLibraryBuilder<>(path -> {
            String content = null;
            try {
                content = getContent(path);
            } catch (Exception e) {
                ExceptionPrinter.printAndExit(new LoadLibraryContentException(e));
            }
            return content;
        }, snippets);
    }

    /*
     * Возвращает контент шейдера или библиотеки при помощи его пути
     */
    public abstract String getContent(T path) throws Exception;
}
