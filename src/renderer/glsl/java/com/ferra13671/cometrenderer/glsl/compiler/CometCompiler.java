package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import com.ferra13671.cometrenderer.glsl.shader.GlShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;

import java.util.*;
import java.util.regex.Pattern;

@API(status = API.Status.EXPERIMENTAL, since = "1.3")
@UtilityClass
public class CometCompiler {
    private final HashMap<String, CompilerExtension> extensions = new HashMap<>();
    public final String DEFAULT_GLSL_FILE_ENTRY = "DEFAULT";

    public void addExtensions(@NonNull CompilerExtension... extensions) {
        for (CompilerExtension extension : extensions) {
            if (CometCompiler.extensions.containsKey(extension.getName()))
                CometRenderer.getLogger().warn(String.format("Found 2 compiler extensions named %s, overwriting prev compiler extension.", extension.getName()));

            CometCompiler.extensions.put(extension.getName(), extension);
        }
    }

    public Optional<CompilerExtension> getExtension(@NonNull String name) {
        return Optional.ofNullable(extensions.get(name));
    }

    public Collection<CompilerExtension> getExtensions() {
        return extensions.values();
    }

    @API(status = API.Status.INTERNAL)
    public GlProgram compileProgram(@NonNull Registry registry) {
        String name = registry.get(CometTags.NAME).orElseThrow();

        int programId = GL20.glCreateProgram();

        Map<ShaderType, GlslFileEntry> shaders = registry.get(CometTags.SHADERS).orElseThrow();
        Map<ShaderType, GlShader> compiledShaders = registry.contains(CometTags.COMPILED_SHADERS) ? registry.get(CometTags.COMPILED_SHADERS).orElseThrow() : null;
        Map<String, UniformType<?>> uniforms = registry.get(CometTags.UNIFORMS).orElseThrow();

        if (compiledShaders != null)
            compiledShaders.forEach((type, shader) -> {
                if (shader.getRegistry().contains(CometTags.UNIFORMS))
                    uniforms.putAll(shader.getRegistry().get(CometTags.UNIFORMS).orElseThrow());

                GL20.glAttachShader(programId, shader.getId());
            });

        shaders.forEach((type, entry) -> {
            GlShader shader = compileShader(entry, type, registry);

            if (entry.getRegistry().contains(CometTags.UNIFORMS))
                uniforms.putAll(entry.getRegistry().get(CometTags.UNIFORMS).orElseThrow());

            GL20.glAttachShader(programId, shader.getId());
        });

        GlProgram program = new GlProgram(name, programId, new HashSet<>(Arrays.asList(registry.get(CometTags.SNIPPETS).orElseThrow())));

        CompileResult compileResult = program.compile(uniforms);

        if (compileResult.isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileProgramException(name, compileResult.message()));

        return program;
    }

    @NonNull
    @API(status = API.Status.INTERNAL)
    public GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType, Registry builderRegistry) {
        //Experimental
        Registry shaderRegistry = new Registry();
        if (builderRegistry.contains(CometTags.TAGS_TO_COPY)) {
            for (Tag<?> tag : builderRegistry.get(CometTags.TAGS_TO_COPY).orElseThrow())
                if (builderRegistry.contains(tag))
                    shaderRegistry.set(builderRegistry.getEntry(tag).orElseThrow());
        }

        GlslFileEntry processedShader = new GlslFileEntry(shaderEntry);
        processContent(processedShader.getRegistry(), builderRegistry);
        GlslContent content = processedShader.getRegistry().get(CometTags.CONTENT).orElseThrow();

        GlShader shader = new GlShader(
                processedShader.getName(),
                shaderType,
                shaderRegistry
        );
        shader.setContent(content);
        shader.compile();

        if (shader.getCompileResult().isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileShaderException(processedShader.getName(), shader.getCompileResult().message()));

        return shader;
    }

    @API(status = API.Status.INTERNAL)
    public void processContent(@NonNull Registry shaderRegistry, @NonNull Registry builderRegistry) {
        removeComments(shaderRegistry);

        RegexCompilerProcessor.processContent(shaderRegistry, builderRegistry);
        for (CompilerExtension extension : getExtensions())
            extension.processCompile(shaderRegistry, builderRegistry);
    }

    @API(status = API.Status.INTERNAL)
    protected void removeComments(@NonNull Registry glslFileRegistry) {
        GlslContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow();

        content.set(Pattern.compile("//.*", Pattern.MULTILINE).matcher(content.concatLines()).replaceAll(""));
        content.set(Pattern.compile("/\\*[\\s\\S]*\\*/", Pattern.MULTILINE).matcher(content.concatLines()).replaceAll(""));
    }
}