package com.ferra13671.cometrenderer.program.schema.snippet;

import com.ferra13671.cometrenderer.program.schema.GlUniformSchema;

import java.util.List;

public record GlProgramSnippet(List<GlUniformSchema> uniforms) {}