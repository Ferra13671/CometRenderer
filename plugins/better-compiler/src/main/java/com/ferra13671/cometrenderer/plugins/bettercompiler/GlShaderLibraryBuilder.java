package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.ShaderLibraryProcessor;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import lombok.NonNull;
import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashMap;

@API(status = API.Status.MAINTAINED, since = "2.5")
public class GlShaderLibraryBuilder<T> extends Builder<GLSLFileEntry> {
    private String name;
    private T libraryPath;
    private boolean singleIncludeOnly = false;
    private final HashMap<String, UniformType<?>> uniforms = new HashMap<>();
    private final CometLoader<T> loader;

    public GlShaderLibraryBuilder(CometLoader<T> loader, GLProgramSnippet... snippets) {
        super("shader library");

        for (GLProgramSnippet snippet : snippets)
            snippet.registry().get(CometTags.UNIFORMS).orElseThrow().forEach(this::uniform);

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

    public GlShaderLibraryBuilder<T> singleIncludeOnly() {
        this.singleIncludeOnly = true;
        return this;
    }

    @NonNull
    public <S extends GLUniform> GlShaderLibraryBuilder<T> uniform(String name, UniformType<S> uniformType) {
        if (this.uniforms.containsKey(name))
            ErrorHandlers.onDoubleUniformAddition(name);

        this.uniforms.put(name, uniformType);
        return this;
    }

    @NonNull
    public GlShaderLibraryBuilder<T> sampler(String name) {
        uniform(name, UniformType.SAMPLER);
        return this;
    }

    @Override
    public GLSLFileEntry build() {
        assertNotNull(this.name, "name");
        assertNotNull(this.libraryPath, "library path");

        Registry registry = new Registry();
        registry.setImmutable(CometTags.UNIFORMS, Collections.unmodifiableMap(this.uniforms));
        registry.setImmutable(ShaderLibraryProcessor.SINGLE_INCLUDE_ONLY, this.singleIncludeOnly);

        return new GLSLFileEntry(
                this.name,
                GLSLContent.fromString(this.loader.getContent(this.libraryPath)),
                BetterCompilerPlugin.SHADER_LIBRARY_GLSL_FILE_ENTRY,
                registry
        );
    }
}
