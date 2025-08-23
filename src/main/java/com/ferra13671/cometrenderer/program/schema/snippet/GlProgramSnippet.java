package com.ferra13671.cometrenderer.program.schema.snippet;

import com.ferra13671.cometrenderer.program.schema.GlUniformSchema;

import java.util.List;

/*
 * Сниппет — своеобразный штамп, который может быть добавлен к программе. Представляет из себя информацию, которую можно быстро добавлять к различным программам.
 */
public record GlProgramSnippet(List<GlUniformSchema> uniforms) {}