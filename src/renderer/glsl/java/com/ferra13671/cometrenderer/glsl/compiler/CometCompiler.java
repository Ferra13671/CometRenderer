package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
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
                ErrorHandlers.onDoubleCompilerExtensionAddition(extension.getName());

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
    public GLProgram compileProgram(@NonNull Registry registry) {
        String name = registry.get(CometTags.NAME).orElseThrow();

        int programId = GL20.glCreateProgram();

        Map<ShaderType, GLShader> compiledShaders = registry.get(CometTags.COMPILED_SHADERS).orElseThrow();
        Map<String, UniformType<?>> uniforms = registry.get(CometTags.UNIFORMS).orElseThrow();

        compiledShaders.forEach((type, shader) -> {
            if (shader.getRegistry().contains(CometTags.UNIFORMS))
                uniforms.putAll(shader.getRegistry().get(CometTags.UNIFORMS).orElseThrow());

            GL20.glAttachShader(programId, shader.getId());
        });

        GLProgram program = new GLProgram(name, programId, new HashSet<>(Arrays.asList(registry.get(CometTags.SNIPPETS).orElseThrow())));

        CompileResult compileResult = program.compile(uniforms);

        if (compileResult.isFailure())
            ErrorHandlers.onCompileProgramError(name, compileResult.message());

        return program;
    }

    @NonNull
    @API(status = API.Status.INTERNAL)
    public GLShader compileShader(GLSLFileEntry shaderEntry, ShaderType shaderType, Registry builderRegistry) {
        //Experimental
        Registry shaderRegistry = new Registry();
        if (builderRegistry.contains(CometTags.TAGS_TO_COPY)) {
            for (Tag<?> tag : builderRegistry.get(CometTags.TAGS_TO_COPY).orElseThrow())
                if (builderRegistry.contains(tag))
                    shaderRegistry.set(builderRegistry.getEntry(tag).orElseThrow());
        }

        GLSLFileEntry processedShader = new GLSLFileEntry(shaderEntry);
        processContent(processedShader.getRegistry(), builderRegistry);
        GLSLContent content = processedShader.getRegistry().get(CometTags.CONTENT).orElseThrow();

        GLShader shader = new GLShader(
                processedShader.getName(),
                shaderType,
                shaderRegistry
        );
        shader.setContent(content);
        shader.compile();

        if (shader.getCompileResult().isFailure())
            ErrorHandlers.onCompileShaderError(processedShader.getName(), shader.getCompileResult().message());

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
        GLSLContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow();

        content.set(Pattern.compile("//.*", Pattern.MULTILINE).matcher(content.concatLines()).replaceAll(""));
        content.set(Pattern.compile("/\\*[\\s\\S]*\\*/", Pattern.MULTILINE).matcher(content.concatLines()).replaceAll(""));
    }
}