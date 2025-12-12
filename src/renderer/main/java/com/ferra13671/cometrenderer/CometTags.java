package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.compiler.tag.Tag;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

import java.util.Map;

public final class CometTags {

    public static final Tag<String> NAME = new Tag<>("name");
    public static final Tag<String> CONTENT = new Tag<>("content");
    public static final Tag<String> TYPE = new Tag<>("type");
    public static final Tag<Map<String, UniformType<?>>> UNIFORMS = new Tag<>("uniforms");
    public static final Tag<GlProgramSnippet[]> SNIPPETS = new Tag<>("snippets");
    public static final Tag<Map<ShaderType, GlslFileEntry>> SHADERS = new Tag<>("shaders");
}
