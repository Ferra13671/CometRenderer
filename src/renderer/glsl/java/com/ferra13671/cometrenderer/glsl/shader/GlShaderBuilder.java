package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.UnsupportedShaderException;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.GlUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@API(status = API.Status.EXPERIMENTAL, since = "2.7")
public class GlShaderBuilder<T> extends Builder<GlShader> {
    private final Registry registry = new Registry();
    private final CometLoader<T> loader;
    private GlslFileEntry entry;
    private ShaderType type;

    public GlShaderBuilder(CometLoader<T> loader) {
        super("shader");

        this.loader = loader;
        this.registry.setImmutable(CometTags.UNIFORMS, new HashMap<>());
        this.registry.setImmutable(CometTags.TAGS_TO_COPY, new ArrayList<>() {{
            add(CometTags.UNIFORMS);
        }});

        for (CompilerExtension extension : GlobalCometCompiler.getExtensions())
            extension.onCreateGlslBuilder(this.registry);
    }

    @NonNull
    public GlShaderBuilder<T> info(String name, T shaderPath, ShaderType type) {
        return info(this.loader.createGlslFileEntry(name, shaderPath), type);
    }

    @NonNull
    public GlShaderBuilder<T> info(GlslFileEntry entry, ShaderType type) {
        if (CometRenderer.getConfig().COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS.getValue()) {
            GLVersion glVersion = CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue();
            if (!type.isSupportedOn(glVersion))
                CometRenderer.getExceptionManager().manageException(new UnsupportedShaderException(glVersion, type.glVersion));
        }

        this.entry = entry;
        this.type = type;

        return this;
    }

    @NonNull
    public <S> GlShaderBuilder<T> tag(@NonNull Tag<S> tag, S value) {
        this.registry.set(tag, value);

        return this;
    }

    public <S> S getFromTag(Tag<S> tag) {
        return this.registry.get(tag).orElseThrow().getValue();
    }

    @NonNull
    public <S extends GlUniform> GlShaderBuilder<T> uniform(String name, UniformType<S> uniformType) {
        Map<String, UniformType<?>> uniforms = this.registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        if (uniforms.containsKey(name))
            CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(name));

        uniforms.put(name, uniformType);
        return this;
    }

    @NonNull
    public GlShaderBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    @Override
    public GlShader build() {
        return GlobalCometCompiler.compileShader(this.entry, this.type, this.registry);
    }
}
