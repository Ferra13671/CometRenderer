package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.builders.GlProgramSnippetBuilder;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/*
 * Сниппет — своеобразный штамп, который может быть добавлен к программе. Представляет из себя информацию, которую можно быстро добавлять к различным программам.
 */
public record GlProgramSnippet(List<GlUniformSchema<?>> uniforms) {

    /*
     * Возвращает новый билдер сниппета
     */
    public static GlProgramSnippetBuilder builder() {
        return new GlProgramSnippetBuilder();
    }
}