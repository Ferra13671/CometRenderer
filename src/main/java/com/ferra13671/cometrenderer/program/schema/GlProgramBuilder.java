package com.ferra13671.cometrenderer.program.schema;

import com.ferra13671.cometrenderer.exceptions.BuildProgramException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.loader.GlProgramLoader;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.List;

/*
 * Билдер схемы программы.
 * Что-то схожее с RenderPipeline.Builder.
 */
public class GlProgramBuilder {
    private String name;
    private ShaderSchema vertexShader;
    private ShaderSchema fragmentShader;
    private final List<GlUniformSchema> uniforms = new ArrayList<>();

    public static GlProgramBuilder builder(GlProgramSnippet... snippets) {
        GlProgramBuilder builder = new GlProgramBuilder();

        for (GlProgramSnippet snippet : snippets)
            builder.uniforms.addAll(snippet.uniforms());

        return builder;
    }

    /*
     * Устанавливает имя программе
     */
    public GlProgramBuilder name(String name) {
        this.name = name;
        return this;
    }

    /*
     * Устанавливает вертексный шейдер программе
     */
    public GlProgramBuilder vertexShader(String name, String vertexShaderId) {
        this.vertexShader = new ShaderSchema(name, vertexShaderId, ShaderType.Vertex);
        return this;
    }

    /*
     * Устанавливает фрагментный шейдер программе
     */
    public GlProgramBuilder fragmentShader(String name, String fragmentShaderId) {
        this.fragmentShader = new ShaderSchema(name, fragmentShaderId, ShaderType.Fragment);
        return this;
    }

    /*
     * Добавляет униформу программе
     */
    public GlProgramBuilder uniform(String name, UniformType uniformType) {
        uniforms.add(new GlUniformSchema(name, uniformType));
        return this;
    }

    /*
     * Добавляет семплер программе
     */
    public GlProgramBuilder sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema(name, UniformType.SAMPLER));
        return this;
    }

    /*
     * Компилирует программу с указанной в билдере информацией
     */
    public GlProgram build() {
        if (name == null)
            throw new BuildProgramException("Missing name in program builder.");
        if (vertexShader == null)
            throw new BuildProgramException(String.format("Missing vertex shader in program '%s'.", name));
        if (fragmentShader == null)
            throw new BuildProgramException(String.format("Missing fragment shader in program '%s'.", name));

        return GlProgramLoader.loadProgram(new GlProgramSchema(name, vertexShader, fragmentShader, uniforms));
    }
}
