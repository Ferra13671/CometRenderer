package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;

import java.util.Map;

public final class CometTags {

    public static final Tag<String> NAME = new Tag<>("name");
    public static final Tag<GlslContent> CONTENT = new Tag<>("content");
    public static final Tag<String> TYPE = new Tag<>("type");
    public static final Tag<Map<String, UniformType<?>>> UNIFORMS = new Tag<>("uniforms");
    public static final Tag<GlProgramSnippet[]> SNIPPETS = new Tag<>("snippets");
    public static final Tag<Map<ShaderType, GlslFileEntry>> SHADERS = new Tag<>("shaders");

    public static final Tag<Boolean> INITIALIZED = new Tag<>("initialized");
    public static final Tag<String> COMET_RENDERER_VERSION = new Tag<>("comet-renderer-version");
    public static final Tag<String> VENDOR = new Tag<>("vendor");
    public static final Tag<String> GPU = new Tag<>("gpu");
    public static final Tag<GLVersion> GL_VERSION = new Tag<>("gl-version");
    public static final Tag<Mesa3DVersion> MESA_VERSION = new Tag<>("mesa-version");
    public static final Tag<String[]> GL_EXTENSIONS = new Tag<>("gl-extensions");
    public static final Tag<Integer> MAX_VERTEX_ELEMENTS = new Tag<>("max-vertex-elements");
    public static final Tag<Boolean> SAMPLER_OBJECT_SUPPORT = new Tag<>("sampler-object-support");
}
