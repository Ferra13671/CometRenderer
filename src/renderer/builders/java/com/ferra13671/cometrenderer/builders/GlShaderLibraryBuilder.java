package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.compile.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalLibraryBuilderArgumentException;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.compile.GlShaderLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Сборщик шейдерной библиотеки.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GlShaderLibrary
 */
public class GlShaderLibraryBuilder<T> {
    /** Имя шейдерной библиотеки. **/
    private String name;
    /** Путь к шейдерной библиотеке. **/
    private T libraryPath;
    /** Список униформ шейдерной библиотеки. **/
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();
    /** Загрузчик контента шейдерной библиотеки. **/
    private final Function<T, String> contentGetter;

    /**
     * @param contentGetter загрузчик контента шейдерной библиотеки.
     * @param snippets фрагменты программы.
     */
    public GlShaderLibraryBuilder(Function<T, String> contentGetter, GlProgramSnippet... snippets) {
        for (GlProgramSnippet snippet : snippets)
            uniforms.addAll(snippet.uniforms());
        this.contentGetter = contentGetter;
    }

    /**
     * Устанавливает имя шейдерной библиотеке.
     *
     * @param name имя шейдерной библиотеки.
     * @return сборщик шейдерной библиотеки.
     */
    public GlShaderLibraryBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Устанавливает путь к шейдерной библиотеке.
     *
     * @param libraryPath путь к шейдерной библиотеке.
     * @return сборщик шейдерной библиотеки.
     */
    public GlShaderLibraryBuilder<T> library(T libraryPath) {
        this.libraryPath = libraryPath;
        return this;
    }

    /**
     * Добавляет униформу шейдерной библиотеке.
     *
     * @param name имя униформы.
     * @param uniformType тип униформы.
     * @return сборщик шейдерной библиотеки.
     * @param <S> униформа.
     *
     * @see GlUniform
     */
    public <S extends GlUniform> GlShaderLibraryBuilder<T> uniform(String name, UniformType<S> uniformType) {
        uniforms.add(new GlUniformSchema<>(name, uniformType));
        return this;
    }

    /**
     * Добавляет семплер библиотеке.
     *
     * @param name имя семплера.
     * @return сборщик шейдерной библиотеки.
     */
    public GlShaderLibraryBuilder<T> sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    /**
     * Собирает все данные в новую шейдерную библиотеку.
     *
     * @return новая шейдерная библиотека.
     *
     * @see GlShaderLibrary
     */
    public GlShaderLibrary build() {
        if (name == null)
            ExceptionPrinter.printAndExit(new IllegalLibraryBuilderArgumentException("Missing name in shader library builder."));
        if (libraryPath == null)
            ExceptionPrinter.printAndExit(new IllegalLibraryBuilderArgumentException(String.format("Missing libraryPath in library '%s'.", name)));

        return new GlShaderLibrary(
                new GlslFileEntry(
                        name,
                        contentGetter.apply(libraryPath)
                ),
                uniforms
        );
    }
}
