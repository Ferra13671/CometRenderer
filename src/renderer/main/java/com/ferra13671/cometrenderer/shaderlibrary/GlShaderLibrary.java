package com.ferra13671.cometrenderer.shaderlibrary;

import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/*
 * Шейдерная библиотека, используемого для того, что бы удобно добавлять код, который используется в нескольких шейдерах, не храня его в них
 */
public record GlShaderLibrary(GlslFileEntry libraryEntry, List<GlUniformSchema<?>> uniforms) {
}
