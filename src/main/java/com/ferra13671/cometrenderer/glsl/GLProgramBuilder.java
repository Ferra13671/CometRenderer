package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
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

    /**
     * @param snippets фрагменты программы.
     *
     * @see GLProgramSnippet
     */
    public GLProgramBuilder(GLProgramSnippet... snippets) {
        super("program");

        this.registry.setImmutable(CometTags.COMPILED_SHADERS, new HashMap<>());
        this.registry.setImmutable(CometTags.UNIFORMS, new HashMap<>());

        for (GLProgramSnippet snippet : snippets)
            snippet.applyTo(this);

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

    @NonNull
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public GLProgramBuilder<T> shader(GLShader shader) {
        Map<ShaderType, GLShader> compiledShaders = this.registry.get(CometTags.COMPILED_SHADERS).orElseThrow();
        ShaderType shaderType = shader.shaderType();

        String containsShaderName = compiledShaders.containsKey(shaderType) ? compiledShaders.get(shaderType).name() : null;
        if (containsShaderName != null)
            ErrorHandlers.onDoubleShaderAddition(shader.name(), shaderType, containsShaderName);

        compiledShaders.put(shader.shaderType(), shader);
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

        Map<ShaderType, GLShader> compiledShaders = this.registry.get(CometTags.COMPILED_SHADERS).orElseThrow();

        if (!compiledShaders.containsKey(ShaderType.Compute)) {
            if (!compiledShaders.containsKey(ShaderType.Vertex))
                ErrorHandlers.onIllegalBuilderArgument(String.format("Missing vertex shader in program '%s'.", this.registry.get(CometTags.NAME).orElseThrow()));
            if (!compiledShaders.containsKey(ShaderType.Fragment))
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
}
