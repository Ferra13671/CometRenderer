package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.shader.GlShader;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.List;
import java.util.Map;

@API(status = API.Status.MAINTAINED, since = "1.9")
@UtilityClass
public class CometTags {
    public final Tag<String> NAME = new Tag<>("name");
    public final Tag<GlslContent> CONTENT = new Tag<>("content");
    public final Tag<String> TYPE = new Tag<>("type");
    public final Tag<Map<String, UniformType<?>>> UNIFORMS = new Tag<>("uniforms");
    public final Tag<GlProgramSnippet[]> SNIPPETS = new Tag<>("snippets");
    public final Tag<Map<ShaderType, GlslFileEntry>> SHADERS = new Tag<>("shaders");
    public final Tag<Map<ShaderType, GlShader>> COMPILED_SHADERS = new Tag<>("compiled-shaders");
    public final Tag<List<Tag<?>>> TAGS_TO_COPY = new Tag<>("tags-to-copy");

    public final Tag<Boolean> INITIALIZED = new Tag<>("initialized");
    public final Tag<String> COMET_RENDERER_VERSION = new Tag<>("comet-renderer-version");
    public final Tag<String> VENDOR = new Tag<>("vendor");
    public final Tag<String> GPU = new Tag<>("gpu");
    public final Tag<GLVersion> GL_VERSION = new Tag<>("gl-version");
    public final Tag<Mesa3DVersion> MESA_VERSION = new Tag<>("mesa-version");
    public final Tag<String[]> GL_EXTENSIONS = new Tag<>("gl-extensions");
    public final Tag<Integer> MAX_VERTEX_ELEMENTS = new Tag<>("max-vertex-elements");
    public final Tag<Integer> MAX_VERTICES = new Tag<>("max-vertices");
    public final Tag<Integer> MAX_INDICES = new Tag<>("max-indices");
    public final Tag<Boolean> SAMPLER_OBJECT_SUPPORT = new Tag<>("sampler-object-support");
}
