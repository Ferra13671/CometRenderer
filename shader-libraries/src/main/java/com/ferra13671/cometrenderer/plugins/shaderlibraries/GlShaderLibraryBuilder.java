package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;

public class GlShaderLibraryBuilder<T> extends Builder<GlslFileEntry> {
    private String name;
    private T libraryPath;
    private final HashMap<String, UniformType<?>> uniforms = new HashMap<>();
    private final CometLoader<T> loader;

    public GlShaderLibraryBuilder(CometLoader<T> loader, GlProgramSnippet... snippets) {
        super("shader library");

        for (GlProgramSnippet snippet : snippets)
            snippet.registry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach(this::uniform);

        this.loader = loader;
    }

    public GlShaderLibraryBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    @NonNull
    public GlShaderLibraryBuilder<T> library(T libraryPath) {
        this.libraryPath = libraryPath;
        return this;
    }

    @NonNull
    public <S extends GlUniform> GlShaderLibraryBuilder<T> uniform(String name, UniformType<S> uniformType) {
        if (this.uniforms.containsKey(name))
            CometRenderer.manageException(new DoubleUniformAdditionException(name));

        this.uniforms.put(name, uniformType);
        return this;
    }

    @NonNull
    public GlShaderLibraryBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    @Override
    public GlslFileEntry build() {
        assertNotNull(this.name, "name");
        assertNotNull(this.libraryPath, "library path");

        Registry registry = new Registry();
        registry.setImmutable(CometTags.UNIFORMS, Collections.unmodifiableMap(this.uniforms));

        return new GlslFileEntry(
                this.name,
                this.loader.getContent(this.libraryPath),
                ShaderLibrariesPlugin.SHADER_LIBRARY_FILE,
                registry
        );
    }
}
