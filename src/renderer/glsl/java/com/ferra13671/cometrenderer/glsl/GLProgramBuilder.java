package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import lombok.NonNull;
import org.apiguardian.api.API;

import java.util.HashMap;
import java.util.Map;

/**
 * Сборщик программы.
 *
 * @param <T> тип объекта, используемого как путь к контенту шейдеров.
 *
 * @see GLProgram
 */
@API(status = API.Status.MAINTAINED, since = "1.1")
public class GLProgramBuilder<T> extends Builder<GLProgram> {
    private final Registry registry = new Registry();
    /** Загрузчик контента шейдеров. **/
    private final CometLoader<T> loader;

    /**
     * @param loader загрузчик контента шейдеров.
     * @param snippets фрагменты программы.
     *
     * @see GLProgramSnippet
     */
    public GLProgramBuilder(CometLoader<T> loader, GLProgramSnippet... snippets) {
        super("program");

        this.registry.setImmutable(CometTags.SHADERS, new HashMap<>());
        this.registry.setImmutable(CometTags.COMPILED_SHADERS, new HashMap<>());
        this.registry.setImmutable(CometTags.UNIFORMS, new HashMap<>());

        for (GLProgramSnippet snippet : snippets)
            snippet.applyTo(this);

        this.loader = loader;

        this.registry.setImmutable(CometTags.SNIPPETS, snippets);

        for (CompilerExtension extension : CometCompiler.getExtensions())
            extension.onCreateGLSLBuilder(this.registry);
    }

    /**
     * Устанавливает имя программе.
     *
     * @param name имя программы.
     * @return сборщик программы.
     */
    public GLProgramBuilder<T> name(String name) {
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
    public GLProgramBuilder<T> shader(String name, T shaderPath, ShaderType type) {
        ErrorHandlers.onLoadShaderWithEmptyBuilder();

        return shader(this.loader.createGLSLFileEntry(name, shaderPath), type);
    }

    /**
     * Добавляет в список шейдеров программы шейдер с данным типом.
     *
     * @param shaderEntry данные шейдера.
     * @param type тип шейдера.
     * @return сборщик программы.
     */
    @NonNull
    public GLProgramBuilder<T> shader(GLSLFileEntry shaderEntry, ShaderType type) {
        if (CometRenderer.getConfig().COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS.getValue()) {
            if (!GLCapabilities.supportsShader(type))
                ErrorHandlers.onUnsupportedShader(type);
        }

        Map<ShaderType, GLSLFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow();
        Map<ShaderType, GLShader> compiledShaders = this.registry.get(CometTags.COMPILED_SHADERS).orElseThrow();

        String containsShaderName = shaders.containsKey(type) ? shaders.get(type).getName() : compiledShaders.containsKey(type) ? compiledShaders.get(type).getName() : null;
        if (containsShaderName != null)
            ErrorHandlers.onDoubleShaderAddition(shaderEntry.getName(), type, containsShaderName);

        shaders.put(type, shaderEntry);
        return this;
    }

    @NonNull
    @API(status = API.Status.EXPERIMENTAL, since = "2.7")
    public GLProgramBuilder<T> shader(GLShader shader) {
        Map<ShaderType, GLSLFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow();
        Map<ShaderType, GLShader> compiledShaders = this.registry.get(CometTags.COMPILED_SHADERS).orElseThrow();
        ShaderType shaderType = shader.getShaderType();

        String containsShaderName = shaders.containsKey(shaderType) ? shaders.get(shaderType).getName() : compiledShaders.containsKey(shaderType) ? compiledShaders.get(shaderType).getName() : null;
        if (containsShaderName != null)
            ErrorHandlers.onDoubleShaderAddition(shader.getName(), shaderType, containsShaderName);

        compiledShaders.put(shader.getShaderType(), shader);
        return this;
    }

    public <S> GLProgramBuilder<T> tag(@NonNull Tag<S> tag, S value) {
        this.registry.set(tag, value);

        return this;
    }

    public <S> S getFromTag(Tag<S> tag) {
        return this.registry.get(tag).orElseThrow();
    }

    /**
     * Добавляет униформу программе.
     *
     * @param name имя униформы.
     * @param uniformType тип униформы
     * @return сборщик программы.
     * @param <S> униформа.
     *
     * @see GLUniform
     */
    @NonNull
    public <S extends GLUniform> GLProgramBuilder<T> uniform(String name, UniformType<S> uniformType) {
        Map<String, UniformType<?>> uniforms = this.registry.get(CometTags.UNIFORMS).orElseThrow();

        if (uniforms.containsKey(name))
            ErrorHandlers.onDoubleUniformAddition(name);

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
    public GLProgramBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    /**
     * Собирает все данные в сборщике в целостную программу.
     *
     * @return программа, скомпилированная сборщиком.
     */
    @Override
    public GLProgram build() {
        assertNotNull(this.registry, CometTags.NAME);

        Map<ShaderType, GLSLFileEntry> shaders = this.registry.get(CometTags.SHADERS).orElseThrow();
        Map<ShaderType, GLShader> compiledShaders = this.registry.get(CometTags.COMPILED_SHADERS).orElseThrow();

        if (!shaders.containsKey(ShaderType.Compute) && !compiledShaders.containsKey(ShaderType.Compute)) {
            if (!shaders.containsKey(ShaderType.Vertex) && !compiledShaders.containsKey(ShaderType.Vertex))
                ErrorHandlers.onIllegalBuilderArgument(String.format("Missing vertex shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow()));
            if (!shaders.containsKey(ShaderType.Fragment) && !compiledShaders.containsKey(ShaderType.Fragment))
                ErrorHandlers.onIllegalBuilderArgument(String.format("Missing fragment shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow()));
        }
        return CometCompiler.compileProgram(this.registry);
    }

    /**
     * Собирает все данные в сборщике в новый фрагмент программы.
     *
     * @implNote Имя, которое вы задали в сборщике, не будет сохранено.
     *
     * @return новый фрагмент программы.
     */
    public GLProgramSnippet buildSnippet() {
        return new GLProgramSnippet(this.registry);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public static GLProgramBuilder<?> empty(GLProgramSnippet... snippets) {
        return new GLProgramBuilder<>(null, snippets);
    }
}
