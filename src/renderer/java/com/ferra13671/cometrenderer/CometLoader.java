package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.exceptions.impl.load.LoadLibraryContentException;
import com.ferra13671.cometrenderer.exceptions.impl.load.LoadShaderContentException;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.builder.GlProgramBuilder;
import com.ferra13671.cometrenderer.program.builder.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippet;

import java.util.function.Function;

/*
 * Лоадер шейдеров и библиотек
 */
public abstract class CometLoader<T> {
    private final Function<T, String> shaderContentGetter = path -> {
        String content = null;
        try {
            content = getContent(path);
        } catch (Exception e) {
            ExceptionPrinter.printAndExit(new LoadShaderContentException(e));
        }
        return content;
    };
    private final Function<T, String> libraryContentGetter = path -> {
        String content = null;
        try {
            content = getContent(path);
        } catch (Exception e) {
            ExceptionPrinter.printAndExit(new LoadLibraryContentException(e));
        }
        return content;
    };

    /*
     * Создает новый билдер программы, использующий данный лоадер
     */
    public GlProgramBuilder<T> createProgramBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(this, snippets);
    }

    /*
     * Создает новый билдер шейдерной библиотеки, использующий данный лоадер
     */
    public GlShaderLibraryBuilder<T> createLibraryBuilder(GlProgramSnippet... snippets) {
        return new GlShaderLibraryBuilder<>(libraryContentGetter, snippets);
    }

    /*
     * Создаёт новые данные шейдера
     */
    public GlslFileEntry createShaderEntry(String name, T path) {
        return GlobalCometCompiler.compileShaderEntry(name, shaderContentGetter, path);
    }

    /*
     * Возвращает контент шейдера или библиотеки при помощи его пути
     */
    public abstract String getContent(T path) throws Exception;
}
