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
import lombok.NonNull;
import org.lwjgl.opengl.GL20;

import java.util.*;

public class GlobalCometCompiler {
    private static final HashMap<String, CompilerExtension> extensions = new HashMap<>();
    public static final String DEFAULT_GLSL_FILE_ENTRY = "DEFAULT";

    public static void addExtensions(@NonNull CompilerExtension... extensions) {
        for (CompilerExtension extension : extensions)
            GlobalCometCompiler.extensions.put(extension.getName(), extension);
    }

    public static Optional<CompilerExtension> getExtension(@NonNull String name) {
        return Optional.ofNullable(extensions.get(name));
    }

    public static Collection<CompilerExtension> getExtensions() {
        return extensions.values();
    }

    public static GlProgram compileProgram(@NonNull Registry registry) {
        String name = registry.get(CometTags.NAME).orElseThrow().getValue();

        int programId = GL20.glCreateProgram();

        Map<ShaderType, GlslFileEntry> shaders = registry.get(CometTags.SHADERS).orElseThrow().getValue();
        Map<String, UniformType<?>> uniforms = registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        for (Map.Entry<ShaderType, GlslFileEntry> shaderEntry : shaders.entrySet()) {
            GlShader shader = compileShader(shaderEntry.getValue(), shaderEntry.getKey(), registry);

            GL20.glAttachShader(programId, shader.id());
        }

        GL20.glLinkProgram(programId);

        GlProgram program = new GlProgram(name, programId, new HashSet<>(Arrays.asList(registry.get(CometTags.SNIPPETS).orElseThrow().getValue())), uniforms);

        CompileResult compileResult = program.getCompileResult();

        if (compileResult.isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileProgramException(name, compileResult.message()));

        return program;
    }

    @NonNull
    public static GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType, Registry programRegistry) {
        processContent(shaderEntry.getRegistry(), programRegistry);
        GlslContent content = shaderEntry.getRegistry().get(CometTags.CONTENT).orElseThrow().getValue();

        int shaderId = GL20.glCreateShader(shaderType.glId);
        GL20.glShaderSource(shaderId, content.concatLines());
        GL20.glCompileShader(shaderId);

        GlShader shader = new GlShader(
                shaderEntry.getName(),
                content,
                shaderId,
                shaderType
        );

        CompileResult compileResult = shader.getCompileResult();

        if (compileResult.isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileShaderException(shaderEntry.getName(), compileResult.message()));

        return shader;
    }

    public static void onCreateProgramBuilder(@NonNull Registry programRegistry) {
        for (CompilerExtension extension : getExtensions())
            extension.onCreateProgramBuilder(programRegistry);
    }

    public static void processContent(@NonNull Registry shaderRegistry, @NonNull Registry programRegistry) {
        removeComments(shaderRegistry);

        GlslDirectiveProcessor.processContent(shaderRegistry, programRegistry);
        for (CompilerExtension extension : getExtensions())
            extension.processCompile(shaderRegistry, programRegistry);
    }

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