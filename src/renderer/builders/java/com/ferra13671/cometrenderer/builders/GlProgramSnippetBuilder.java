package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик фрагмента программы.
 *
 * @see GlProgramSnippet
 */
public class GlProgramSnippetBuilder {
    private final List<GlUniformSchema<?>> uniforms = new ArrayList<>();

    /**
     * Добавляет униформу фрагменту программы.
     *
     * @param name имя униформы.
     * @param uniformType тип униформы
     * @return сборщик программы.
     * @param <T> униформа.
     */
    public <T extends GlUniform> GlProgramSnippetBuilder uniform(String name, UniformType<T> uniformType) {
        uniforms.add(new GlUniformSchema<>(name, uniformType));
        return this;
    }

    /**
     * Добавляет семплер фрагменту программы.
     *
     * @param name имя семплера.
     * @return сборщик программы.
     */
    public GlProgramSnippetBuilder sampler(String name) {
        //Да, семплер это тоже униформа
        uniforms.add(new GlUniformSchema<>(name, UniformType.SAMPLER));
        return this;
    }

    /**
     * Собирает данные в сборщике в целостный фрагмент программы.
     *
     * @return фрагмент программы.
     */
    public GlProgramSnippet build() {
        return new GlProgramSnippet(uniforms);
    }
}
