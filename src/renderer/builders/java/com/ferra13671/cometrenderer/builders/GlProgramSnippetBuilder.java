package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.List;

public class GlProgramSnippetBuilder {
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();

    /*
     * Добавляет униформу элементу программы
     */
    public <T extends GlUniform> GlProgramSnippetBuilder uniform(String name, UniformType<T> uniformType) {
        uniforms.add(new GlUniformSchema<>(name, uniformType));
        return this;
    }

    /*
     * Добавляет семплер программы
     */
    public GlProgramSnippetBuilder sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    public GlProgramSnippet build() {
        return new GlProgramSnippet(uniforms);
    }
}
