package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.tag.Registry;
import com.ferra13671.cometrenderer.compiler.tag.Tag;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleShaderAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramBuilderArgumentException;
import com.ferra13671.cometrenderer.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сборщик программы.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GlProgram
 */
public class GlProgramBuilder<T> {
    private final Registry registry = new Registry();
    /** Загрузчик контента шейдеров. **/
    private final CometLoader<T> loader;

    /**
     * @param loader загрузчик контента шейдеров.
     * @param snippets фрагменты программы.
     *
     * @see GlProgramSnippet
     */
    public GlProgramBuilder(CometLoader<T> loader, GlProgramSnippet... snippets) {
        this.registry.addImmutable(CometTags.SHADERS, new HashMap<>());
        this.registry.addImmutable(CometTags.UNIFORMS, new HashMap<>());

        for (GlProgramSnippet snippet : snippets)
            snippet.applyTo(this);

        this.loader = loader;

        this.registry.addImmutable(CometTags.SNIPPETS, snippets);
    }

    /**
     * Устанавливает имя программе.
     *
     * @param name имя программы.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> name(String name) {
        if (name != null)
            this.registry.add(CometTags.NAME, name);

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
        Map<ShaderType, GlslFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow().getValue();

        if (shaders.containsKey(type))
            ExceptionPrinter.printAndExit(new DoubleShaderAdditionException(name, type, shaders.get(type).getName()));

        shaders.put(type, loader.createGlslFileEntry(name, shaderPath));
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
        Map<ShaderType, GlslFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow().getValue();

        if (shaders.containsKey(type))
            ExceptionPrinter.printAndExit(new DoubleShaderAdditionException(shaderEntry.getName(), type, shaders.get(type).getName()));

        shaders.put(type, shaderEntry);
        return this;
    }

    public <S> GlProgramBuilder<T> tag(Tag<S> tag, S value) {
        this.registry.add(tag, value);

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
        Map<String, UniformType<?>> uniforms = this.registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        if (uniforms.containsKey(name))
            ExceptionPrinter.printAndExit(new DoubleUniformAdditionException(name));

        uniforms.put(name, uniformType);
        return this;
    }

    /**
     * Добавляет семплер программе.
     *
     * @param name имя семплера.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    /**
     * Собирает все данные в сборщике в целостную программу.
     *
     * @return программа, скомпилированная сборщиком.
     */
    public GlProgram build() {
        if (!this.registry.contains(CometTags.NAME))
            ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException("Missing name in program builder."));

        Map<ShaderType, GlslFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow().getValue();

        if (!shaders.containsKey(ShaderType.Compute)) {
            if (!shaders.containsKey(ShaderType.Vertex))
                ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing vertex shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow().getValue())));
            if (!shaders.containsKey(ShaderType.Fragment))
                ExceptionPrinter.printAndExit(new IllegalProgramBuilderArgumentException(String.format("Missing fragment shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow().getValue())));
        }

        List<GlShader> shaderList = new ArrayList<>();
        shaders.forEach((type, glslFileEntry) ->
            shaderList.add(GlobalCometCompiler.compileShader(glslFileEntry, type))
        );

        return GlobalCometCompiler.compileProgram(
                this.registry,
                shaderList
        );
    }

    /**
     * Собирает все данные в сборщике в новый фрагмент программы.
     *
     * @implNote Имя, которое вы задали в сборщике, не будет сохранено.
     *
     * @return новый фрагмент программы.
     */
    public GlProgramSnippet buildSnippet() {
        return new GlProgramSnippet(this.registry);
    }
}
