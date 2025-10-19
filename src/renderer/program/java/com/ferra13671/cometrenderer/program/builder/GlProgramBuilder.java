package com.ferra13671.cometrenderer.program.builder;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramBuilderArgumentException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.global.GlobalCometLoader;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.List;

/*
 * Билдер схемы программы.
 * Что-то схожее с RenderPipeline.Builder.
 */
public class GlProgramBuilder<T> {
    private String name;
    private ShaderSchema vertexShader;
    private ShaderSchema fragmentShader;
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();
    private final CometLoader<T> loader;

    public GlProgramBuilder(CometLoader<T> loader, GlProgramSnippet... snippets) {
        for (GlProgramSnippet snippet : snippets)
            uniforms.addAll(snippet.uniforms());
        this.loader = loader;
    }

    /*
     * Устанавливает имя программе
     */
    public GlProgramBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    /*
     * Устанавливает вертексный шейдер программе
     */
    public GlProgramBuilder<T> vertexShader(String name, T shaderPath) {
        this.vertexShader = new ShaderSchema(loader.createShaderEntry(name, shaderPath), ShaderType.Vertex);
        return this;
    }

    /*
     * Устанавливает вертексный шейдер программе
     */
    public GlProgramBuilder<T> vertexShader(GlslFileEntry shaderEntry) {
        this.vertexShader = new ShaderSchema(shaderEntry, ShaderType.Vertex);
        return this;
    }

    /*
     * Устанавливает фрагментный шейдер программе
     */
    public GlProgramBuilder<T> fragmentShader(String name, T shaderPath) {
        this.fragmentShader = new ShaderSchema(loader.createShaderEntry(name, shaderPath), ShaderType.Fragment);
        return this;
    }

    /*
     * Устанавливает фрагментный шейдер программе
     */
    public GlProgramBuilder<T> fragmentShader(GlslFileEntry shaderEntry) {
        this.fragmentShader = new ShaderSchema(shaderEntry, ShaderType.Fragment);
        return this;
    }

    /*
     * Добавляет униформу программе
     */
    public <S extends GlUniform> GlProgramBuilder<T> uniform(String name, UniformType<S> uniformType) {
        uniforms.add(new GlUniformSchema<>(name, uniformType));
        return this;
    }

    /*
     * Добавляет семплер программе
     */
    public GlProgramBuilder<T> sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    /*
     * Компилирует программу с указанной в билдере информацией
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
