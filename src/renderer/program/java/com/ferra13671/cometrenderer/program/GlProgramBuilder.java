package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.exceptions.impl.UnsupportedShaderException;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import com.ferra13671.cometrenderer.program.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleShaderAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalBuilderArgumentException;
import com.ferra13671.cometrenderer.program.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Сборщик программы.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GlProgram
 */
public class GlProgramBuilder<T> extends Builder<GlProgram> {
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
        super("program");

        this.registry.setImmutable(CometTags.SHADERS, new HashMap<>());
        this.registry.setImmutable(CometTags.UNIFORMS, new HashMap<>());

        for (GlProgramSnippet snippet : snippets)
            snippet.applyTo(this);

        this.loader = loader;

        this.registry.setImmutable(CometTags.SNIPPETS, snippets);
    }

    /**
     * Устанавливает имя программе.
     *
     * @param name имя программы.
     * @return сборщик программы.
     */
    public GlProgramBuilder<T> name(String name) {
        if (name != null)
            this.registry.set(CometTags.NAME, name);

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
    @NonNull
    public GlProgramBuilder<T> shader(String name, T shaderPath, ShaderType type) {
        return shader(this.loader.createGlslFileEntry(name, shaderPath), type);
    }

    /**
     * Добавляет в список шейдеров программы шейдер с данным типом.
     *
     * @param shaderEntry данные шейдера.
     * @param type тип шейдера.
     * @return сборщик программы.
     */
    @NonNull
    public GlProgramBuilder<T> shader(GlslFileEntry shaderEntry, ShaderType type) {
        if (CometRenderer.getConfig().COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS.getValue()) {
            GLVersion glVersion = CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue();
            if (!type.isSupportedOn(glVersion))
                CometRenderer.getExceptionManager().manageException(new UnsupportedShaderException(glVersion, type.glVersion));
        }

        Map<ShaderType, GlslFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow().getValue();

        if (shaders.containsKey(type))
            CometRenderer.getExceptionManager().manageException(new DoubleShaderAdditionException(shaderEntry.getName(), type, shaders.get(type).getName()));

        shaders.put(type, shaderEntry);
        return this;
    }

    public <S> GlProgramBuilder<T> tag(@NonNull Tag<S> tag, S value) {
        this.registry.set(tag, value);

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
    @NonNull
    public <S extends GlUniform> GlProgramBuilder<T> uniform(String name, UniformType<S> uniformType) {
        Map<String, UniformType<?>> uniforms = this.registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        if (uniforms.containsKey(name))
            CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(name));

        uniforms.put(name, uniformType);
        return this;
    }

    /**
     * Добавляет семплер программе.
     *
     * @param name имя семплера.
     * @return сборщик программы.
     */
    @NonNull
    public GlProgramBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    /**
     * Собирает все данные в сборщике в целостную программу.
     *
     * @return программа, скомпилированная сборщиком.
     */
    @Override
    public GlProgram build() {
        assertNotNull(this.registry, CometTags.NAME);

        Map<ShaderType, GlslFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow().getValue();

        //TODO Move from builder?
        if (!shaders.containsKey(ShaderType.Compute)) {
            if (!shaders.containsKey(ShaderType.Vertex))
                CometRenderer.getExceptionManager().manageException(new IllegalBuilderArgumentException("program", String.format("Missing vertex shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow().getValue())));
            if (!shaders.containsKey(ShaderType.Fragment))
                CometRenderer.getExceptionManager().manageException(new IllegalBuilderArgumentException("program", String.format("Missing fragment shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow().getValue())));
        }
        return GlobalCometCompiler.compileProgram(this.registry);
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
