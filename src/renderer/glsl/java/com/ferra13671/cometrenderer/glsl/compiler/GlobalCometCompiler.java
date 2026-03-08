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
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;

import java.util.*;

@API(status = API.Status.EXPERIMENTAL, since = "1.3")
public class GlobalCometCompiler {
    private static final HashMap<String, CompilerExtension> extensions = new HashMap<>();
    public static final String DEFAULT_GLSL_FILE_ENTRY = "DEFAULT";

    public static void addExtensions(@NonNull CompilerExtension... extensions) {
        for (CompilerExtension extension : extensions) {
            if (GlobalCometCompiler.extensions.containsKey(extension.getName()))
                CometRenderer.getLogger().warn(String.format("Found 2 compiler extensions named %s, overwriting prev compiler extension.", extension.getName()));

            GlobalCometCompiler.extensions.put(extension.getName(), extension);
        }
    }

    public static Optional<CompilerExtension> getExtension(@NonNull String name) {
        return Optional.ofNullable(extensions.get(name));
    }

    public static Collection<CompilerExtension> getExtensions() {
        return extensions.values();
    }

    @API(status = API.Status.INTERNAL)
    public static GlProgram compileProgram(@NonNull Registry registry) {
        String name = registry.get(CometTags.NAME).orElseThrow().getValue();

        int programId = GL20.glCreateProgram();

        Map<ShaderType, GlslFileEntry> shaders = registry.get(CometTags.SHADERS).orElseThrow().getValue();
        Map<ShaderType, GlShader> compiledShaders = registry.contains(CometTags.COMPILED_SHADERS) ? registry.get(CometTags.COMPILED_SHADERS).orElseThrow().getValue() : null;
        Map<String, UniformType<?>> uniforms = registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        if (compiledShaders != null)
            compiledShaders.forEach((type, shader) -> {
                if (shader.getRegistry().contains(CometTags.UNIFORMS))
                    uniforms.putAll(shader.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue());

                GL20.glAttachShader(programId, shader.getId());
            });

        shaders.forEach((type, entry) -> {
            GlShader shader = compileShader(entry, type, registry);

            if (entry.getRegistry().contains(CometTags.UNIFORMS))
                uniforms.putAll(entry.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue());

            GL20.glAttachShader(programId, shader.getId());
        });

        GL20.glLinkProgram(programId);

        GlProgram program = new GlProgram(name, programId, new HashSet<>(Arrays.asList(registry.get(CometTags.SNIPPETS).orElseThrow().getValue())), uniforms);

        CompileResult compileResult = program.getCompileResult();

        if (compileResult.isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileProgramException(name, compileResult.message()));

        return program;
    }

    @NonNull
    @API(status = API.Status.INTERNAL)
    public static GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType, Registry builderRegistry) {
        //Experimental
        Registry shaderRegistry = new Registry();
        if (builderRegistry.contains(CometTags.TAGS_TO_COPY)) {
            for (Tag<?> tag : builderRegistry.get(CometTags.TAGS_TO_COPY).orElseThrow().getValue())
                if (builderRegistry.contains(tag))
                    shaderRegistry.set(builderRegistry.get(tag).orElseThrow());
        }

        GlslFileEntry processedShader = new GlslFileEntry(shaderEntry);
        processContent(processedShader.getRegistry(), builderRegistry);
        GlslContent content = processedShader.getRegistry().get(CometTags.CONTENT).orElseThrow().getValue();

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
    public static void processContent(@NonNull Registry shaderRegistry, @NonNull Registry builderRegistry) {
        removeComments(shaderRegistry);

        GlslDirectiveProcessor.processContent(shaderRegistry, builderRegistry);
        for (CompilerExtension extension : getExtensions())
            extension.processCompile(shaderRegistry, builderRegistry);
    }

    @API(status = API.Status.INTERNAL)
    protected static void removeComments(@NonNull Registry glslFileRegistry) {
        List<String> l = new ArrayList<>();
        GlslContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        boolean comment = false;
        for (String line : content.getLines()) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                if (
                        ch == '/'
                        && i < line.length() - 1
                ) {
                    char c = line.charAt(i + 1);

                    if (c == '/')
                        if (!comment)
                            break;

                    if (c == '*')
                        comment = true;
                }

                if (
                        ch == '*'
                        && i < line.length() - 1
                        && line.charAt(i + 1) == '/'
                ) {
                    comment = false;
                    i += 1;
                    continue;
                }

                if (!comment)
                    builder.append(ch);
            }

            if (!builder.isEmpty() || !comment)
                l.add(builder.toString());
        }

        content.setLines(l.toArray(new String[0]));
    }
}