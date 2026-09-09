package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@API(status = API.Status.EXPERIMENTAL, since = "2.7")
public class GLShaderBuilder<T> extends Builder<GLShader> {
    private final Registry registry = new Registry();
    private final CometLoader<T> loader;
    private GLSLFileEntry entry;
    private ShaderType type;

    public GLShaderBuilder(CometLoader<T> loader) {
        super("shader");

        this.loader = loader;
        this.registry.setImmutable(CometTags.UNIFORMS, new HashMap<>());
        this.registry.setImmutable(CometTags.TAGS_TO_COPY, new ArrayList<>() {{
            add(CometTags.UNIFORMS);
        }});

        for (CompilerExtension extension : CometCompiler.getExtensions())
            extension.onCreateGLSLBuilder(this.registry);
    }

    @NonNull
    public GLShaderBuilder<T> info(String name, T shaderPath, ShaderType type) {
        return info(this.loader.createGLSLFileEntry(name, shaderPath), type);
    }

    @NonNull
    public GLShaderBuilder<T> info(GLSLFileEntry entry, ShaderType type) {
        if (CometRenderer.getConfig().COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS.getValue()) {
            if (!GLCapabilities.supportsShader(type))
                ErrorHandlers.onUnsupportedShader(type);
        }

        this.entry = entry;
        this.type = type;

        return this;
    }

    @NonNull
    public <S> GLShaderBuilder<T> tag(@NonNull Tag<S> tag, S value) {
        this.registry.set(tag, value);

        return this;
    }

    public <S> S getFromTag(Tag<S> tag) {
        return this.registry.get(tag).orElseThrow();
    }

    @NonNull
    public <S extends GLUniform> GLShaderBuilder<T> uniform(String name, UniformType<S> uniformType) {
        Map<String, UniformType<?>> uniforms = this.registry.get(CometTags.UNIFORMS).orElseThrow();

        if (uniforms.containsKey(name))
            ErrorHandlers.onDoubleUniformAddition(name);

        uniforms.put(name, uniformType);
        return this;
    }

    @NonNull
    public GLShaderBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    @Override
    public GLShader build() {
        return CometCompiler.compileShader(this.entry, this.type, this.registry);
    }
}
