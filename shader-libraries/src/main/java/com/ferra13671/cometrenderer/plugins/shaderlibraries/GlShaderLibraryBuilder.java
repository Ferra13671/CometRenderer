package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.Collections;
import java.util.HashMap;

public class GlShaderLibraryBuilder<T> {
    private String name;
    private T libraryPath;
    private final HashMap<String, UniformType<?>> uniforms = new HashMap<>();
    private final CometLoader<T> loader;

    public GlShaderLibraryBuilder(CometLoader<T> loader, GlProgramSnippet... snippets) {
        for (GlProgramSnippet snippet : snippets)
            snippet.registry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach(this::uniform);

        this.loader = loader;
    }

    public GlShaderLibraryBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public GlShaderLibraryBuilder<T> library(T libraryPath) {
        this.libraryPath = libraryPath;
        return this;
    }

    public <S extends GlUniform> GlShaderLibraryBuilder<T> uniform(String name, UniformType<S> uniformType) {
        if (this.uniforms.containsKey(name))
            ExceptionPrinter.printAndExit(new DoubleUniformAdditionException(name));

        this.uniforms.put(name, uniformType);
        return this;
    }

    public GlShaderLibraryBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    public GlslFileEntry build() {
        if (this.name == null)
            throw new IllegalArgumentException("Missing name in shader library builder.");
        if (this.libraryPath == null)
            throw new IllegalArgumentException(String.format("Missing libraryPath in library '%s'.", name));

        Registry registry = new Registry();
        registry.addImmutable(CometTags.UNIFORMS, Collections.unmodifiableMap(this.uniforms));

        return new GlslFileEntry(
                this.name,
                this.loader.getContent(this.libraryPath),
                ShaderLibrariesPlugin.SHADER_LIBRARY_FILE,
                registry
        );
    }
}
