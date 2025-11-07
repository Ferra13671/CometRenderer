package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.load.LoadShaderLibraryContentException;
import com.ferra13671.cometrenderer.exceptions.impl.load.LoadGlslContentException;
import com.ferra13671.cometrenderer.builders.GlProgramBuilder;
import com.ferra13671.cometrenderer.builders.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;

import java.util.function.Function;

/**
 * Объект, представляющий собой загрузчик glsl контента.
 * Загрузчик позволяет загружать glsl контент с нестандартных путей (всё зависит от реализации загрузки).
 * Основные загрузчики есть в {@link CometLoaders}.
 *
 * @param <T> тип пути, при помощи которого будет возвращаться glsl контент.
 */
public abstract class CometLoader<T> {
    private final Function<T, String> glslContentGetter = path -> {
        String content = null;
        try {
            content = getContent(path);
        } catch (Exception e) {
            ExceptionPrinter.printAndExit(new LoadGlslContentException(e));
        }
        return content;
    };
    private final Function<T, String> shaderLibraryContentGetter = path -> {
        String content = null;
        try {
            content = getContent(path);
        } catch (Exception e) {
            ExceptionPrinter.printAndExit(new LoadShaderLibraryContentException(e));
        }
        return content;
    };

    /**
     * Создаёт новый сборщик программы, который будет использовать данный загрузчик.
     *
     * @param snippets фрагменты программы.
     * @return новый сборщик программы.
     *
     * @see GlProgramBuilder
     * @see GlProgram
     * @see GlProgramSnippet
     */
    public GlProgramBuilder<T> createProgramBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(this, snippets);
    }

    /**
     * Создаёт новый сборщик шейдерной библиотеки, который будет использовать данный загрузчик.
     *
     * @param snippets фрагменты программы, которые будут добавляться в программу при добавлении библиотеки в шейдер.
     * @return новый сборщик шейдерной библиотеки.
     *
     * @see GlShaderLibraryBuilder
     * @see GlShaderLibrary
     */
    public GlShaderLibraryBuilder<T> createShaderLibraryBuilder(GlProgramSnippet... snippets) {
        return new GlShaderLibraryBuilder<>(shaderLibraryContentGetter, snippets);
    }

    /**
     * Создаёт новый glsl контент.
     *
     * @param name имя glsl контента.
     * @param path путь к glsl контенту.
     * @return новые glsl данные, хранящие заготовленный контент.
     *
     * @see GlslFileEntry
     */
    public GlslFileEntry createGlslFileEntry(String name, T path) {
        return new GlslFileEntry(name, glslContentGetter.apply(path));
    }

    /**
     * Возвращает контент, загруженный при помощи данного пути.
     *
     * @param path путь, при помощи которого нужно получить контент.
     * @return контент, загруженный при помощи данного пути.
     * @throws Exception различные проблемы, которые могут произойти при загрузке.
     */
    public abstract String getContent(T path) throws Exception;
}
