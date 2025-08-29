package com.ferra13671.cometrenderer.program.builder;

import com.ferra13671.cometrenderer.global.GlobalCometLoader;
import com.ferra13671.cometrenderer.exceptions.BuildLibraryException;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GlShaderLibraryBuilder<T> {
    private String name;
    private T libraryPath;
    private final List<GlUniformSchema> uniforms = new ArrayList<>();
    private final Function<T, String> contentGetter;

    public GlShaderLibraryBuilder(Function<T, String> contentGetter, GlProgramSnippet... snippets) {
        for (GlProgramSnippet snippet : snippets)
            uniforms.addAll(snippet.uniforms());
        this.contentGetter = contentGetter;
    }

    /*
     * Устанавливает имя библиотеке
     */
    public GlShaderLibraryBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    /*
     * Указываем билдеру буть для получения библиотеки
     */
    public GlShaderLibraryBuilder<T> library(T libraryPath) {
        this.libraryPath = libraryPath;
        return this;
    }

    /*
     * Добавляет униформу библиотеке
     */
    public GlShaderLibraryBuilder<T> uniform(String name, UniformType uniformType) {
        uniforms.add(new GlUniformSchema(name, uniformType));
        return this;
    }

    /*
     * Добавляет семплер библиотеке
     */
    public GlShaderLibraryBuilder<T> sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema(name, UniformType.SAMPLER));
        return this;
    }

    /*
     * Компилирует шейдерную библиотеку с указанной в билдере информацией
     */
    public GlShaderLibrary build() {
        if (name == null)
            throw new BuildLibraryException("Missing name in shader library builder.");
        if (libraryPath == null)
            throw new BuildLibraryException(String.format("Missing libraryPath in library '%s'.", name));

        return GlobalCometLoader.loadLibrary(new GlShaderLibrarySchema<>(name, contentGetter, libraryPath, uniforms));
    }
}
