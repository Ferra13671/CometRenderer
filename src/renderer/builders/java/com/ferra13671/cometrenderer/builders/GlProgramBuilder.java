package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.compile.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleShaderAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramBuilderArgumentException;
import com.ferra13671.cometrenderer.compile.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Сборщик программы.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GlProgram
 */
public class GlProgramBuilder<T> {
    /** Имя программы. **/
    private String name;
    /** Карта всех добавленных шейдеров по их типу. **/
    private final HashMap<ShaderType, GlslFileEntry> shaders = new HashMap<>();
    /** Список униформ программы. **/
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();
    /** Фрагменты программы, которые будут добавлены в программу. **/
    private final GlProgramSnippet[] snippets;
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
            this.uniforms.addAll(snippet.uniforms());
        this.loader = loader;
        this.snippets = snippets;
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
     * Добавляет в список шейдеров программы шейдер с данным типом.
     *
     * @param name имя шейдера.
     * @param shaderPath путь к шейдеру.
     * @param type тип шейдера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> shader(String name, T shaderPath, ShaderType type) {
        if (this.shaders.containsKey(type))
            ExceptionPrinter.printAndExit(new DoubleShaderAdditionException(name, type, this.shaders.get(type).name()));

        this.shaders.put(type, loader.createGlslFileEntry(name, shaderPath));
        return this;
    }

    /**
     * Добавляет в список шейдеров программы шейдер с данным типом.
     *
     * @param shaderEntry данные шейдера.
     * @param type тип шейдера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> shader(GlslFileEntry shaderEntry, ShaderType type) {
        if (this.shaders.containsKey(type))
            ExceptionPrinter.printAndExit(new DoubleShaderAdditionException(name, type, this.shaders.get(type).name()));

        this.shaders.put(type, shaderEntry);
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
        this.uniforms.add(new GlUniformSchema<>(name, uniformType));
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
        this.uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    /**
     * Собирает все данные в сборщике в целостную программу.
     *
     * @return программа, скомпилированная сборщиком.
     */
    public GlProgram build() {
        if (this.name == null)
            ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException("Missing name in program builder."));

        if (!this.shaders.containsKey(ShaderType.Compute)) {
            if (!this.shaders.containsKey(ShaderType.Vertex))
                ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing vertex shader in program '%s'.", this.name)));
            if (!this.shaders.containsKey(ShaderType.Fragment))
                ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing fragment shader in program '%s'.", this.name)));
        }

        List<GlShader> shaderList = new ArrayList<>();
        this.shaders.forEach((type, glslFileEntry) ->
            shaderList.add(GlobalCometCompiler.compileShader(glslFileEntry, type))
        );

        return GlobalCometCompiler.compileProgram(
                this.name,
                shaderList,
                this.snippets,
                this.uniforms
        );
    }
}
