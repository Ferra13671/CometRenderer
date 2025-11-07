package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramBuilderArgumentException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.global.GlobalCometLoader;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик программы.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GlProgram
 * @see GlProgramSchema
 */
//TODO возможность добавлять в программу другие шейдеры, кроме вершинного и фрагментного.
public class GlProgramBuilder<T> {
    /** Имя программы. **/
    private String name;
    /** Вершинный шейдер. **/
    private ShaderSchema vertexShader;
    /** Фрагментный шейдер. **/
    private ShaderSchema fragmentShader;
    /** Список униформ программы. **/
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();
    /** Загрузчик контента шейдеров. **/
    private final CometLoader<T> loader;

    /**
     * @param loader загрузчик контента шейдеров.
     * @param snippets фрагменты программы.
     *
     * @see GlProgramSnippet
     */
    public GlProgramBuilder(CometLoader<T> loader, GlProgramSnippet... snippets) {
        for (GlProgramSnippet snippet : snippets)
            uniforms.addAll(snippet.uniforms());
        this.loader = loader;
    }

    /**
     * Устанавливает имя программе.
     *
     * @param name имя программы.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Устанавливает вершинный шейдер программе.
     *
     * @param name имя вершинного шейдера.
     * @param shaderPath путь к вершинному шейдеру.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> vertexShader(String name, T shaderPath) {
        this.vertexShader = new ShaderSchema(loader.createGlslFileEntry(name, shaderPath), ShaderType.Vertex);
        return this;
    }

    /**
     * Устанавливает вершинный шейдер программе.
     *
     * @param shaderEntry данные вершинного шейдера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> vertexShader(GlslFileEntry shaderEntry) {
        this.vertexShader = new ShaderSchema(shaderEntry, ShaderType.Vertex);
        return this;
    }

    /**
     * Устанавливает фрагментный шейдер программе.
     *
     * @param name имя фрагментного шейдера.
     * @param shaderPath путь к фрагментному шейдеру.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> fragmentShader(String name, T shaderPath) {
        this.fragmentShader = new ShaderSchema(loader.createGlslFileEntry(name, shaderPath), ShaderType.Fragment);
        return this;
    }

    /**
     * Устанавливает фрагментный шейдер программе.
     *
     * @param shaderEntry данные фрагментного шейдера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> fragmentShader(GlslFileEntry shaderEntry) {
        this.fragmentShader = new ShaderSchema(shaderEntry, ShaderType.Fragment);
        return this;
    }

    /**
     * Добавляет униформу программе.
     *
     * @param name имя униформы.
     * @param uniformType тип униформы
     * @return сборщик программы.
     * @param <S> униформа.
     *
     * @see GlUniform
     */
    public <S extends GlUniform> GlProgramBuilder<T> uniform(String name, UniformType<S> uniformType) {
        uniforms.add(new GlUniformSchema<>(name, uniformType));
        return this;
    }

    /**
     * Добавляет семплер программе.
     *
     * @param name имя семплера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    /**
     * Собирает все данные в сборщике в целостную программу.
     *
     * @return программа, скомпилированная сборщиком.
     */
    public GlProgram build() {
        if (name == null)
            ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException("Missing name in program builder."));
        if (vertexShader == null)
            ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing vertex shader in program '%s'.", name)));
        if (fragmentShader == null)
            ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing fragment shader in program '%s'.", name)));

        return GlobalCometLoader.loadProgram(new GlProgramSchema(name, vertexShader, fragmentShader, uniforms));
    }
}
