package com.ferra13671.cometrenderer.shaderlibrary;

import com.ferra13671.cometrenderer.program.builder.GlUniformSchema;

import java.util.List;

public record GlShaderLibrary(String name, String libraryContent, List<GlUniformSchema> uniforms) {
}
