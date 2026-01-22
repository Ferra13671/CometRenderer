package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.DefaultTagEntry;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
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

/*
    TODO
        'extra-compiler' plugin
        directive processor
 */
public class GlobalCometCompiler {
    private static final List<CompilerExtension> extensions = new ArrayList<>();

    public static final String DEFAULT_FILE = "DEFAULT";

    public static void addExtensions(@NonNull CompilerExtension... extensions) {
        GlobalCometCompiler.extensions.addAll(List.of(extensions));
    }

    @NonNull
    public static GlProgram compileProgram(Registry registry) {
        String name = registry.get(CometTags.NAME).orElseThrow().getValue();

        int programId = GL20.glCreateProgram();

        Map<ShaderType, GlslFileEntry> shaders = registry.get(CometTags.SHADERS).orElseThrow().getValue();
        Map<String, UniformType<?>> uniforms = registry.get(CometTags.UNIFORMS).orElseThrow().getValue();

        for (Map.Entry<ShaderType, GlslFileEntry> shaderEntry : shaders.entrySet()) {
            GlShader shader = compileShader(shaderEntry.getValue(), shaderEntry.getKey(), registry);

            GL20.glAttachShader(programId, shader.id());

            shader.extraUniforms().forEach((s, uniformType) -> {
                if (uniforms.containsKey(s))
                    CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(s));

                uniforms.put(s, uniformType);
            });
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
        for (CompilerExtension extension : extensions)
            extension.modify(shaderEntry.getRegistry(), programRegistry);
        String content = shaderEntry.getRegistry().get(CometTags.CONTENT).orElseThrow().getValue();

        int shaderId = GL20.glCreateShader(shaderType.glId);
        GL20.glShaderSource(shaderId, content);
        GL20.glCompileShader(shaderId);

        GlShader shader = new GlShader(
                shaderEntry.getName(),
                content,
                shaderId,
                shaderEntry.getRegistry().get(CometTags.UNIFORMS).orElse(new DefaultTagEntry<>(CometTags.UNIFORMS, new HashMap<>())).getValue(),
                shaderType
        );

        CompileResult compileResult = shader.getCompileResult();

        if (compileResult.isFailure())
            CometRenderer.getExceptionManager().manageException(new CompileShaderException(shaderEntry.getName(), compileResult.message()));

        return shader;
    }
}