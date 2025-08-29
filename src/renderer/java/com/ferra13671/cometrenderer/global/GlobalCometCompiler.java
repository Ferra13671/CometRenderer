package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.exceptions.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.IllegalShaderFormatException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.builder.GlUniformSchema;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatusChecker;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibraries;
import org.lwjgl.opengl.GL20;

import java.util.List;

/*
 * Глобальный компилятор
 */
public class GlobalCometCompiler {
    private static final String includeLibAction = "#include";

    /*
     * Компилирует программу
     */
    public static GlProgram compileProgram(String name, GlShader vertexShader, GlShader fragmentShader, List<GlUniformSchema> uniforms) {
        if (vertexShader.getShaderType() != ShaderType.Vertex)
            throw new IllegalShaderFormatException(String.format("'%s' is not a vertex shader.", vertexShader.getName()));
        if (fragmentShader.getShaderType() != ShaderType.Fragment)
            throw new IllegalShaderFormatException(String.format("'%s' is not a fragment shader.", fragmentShader.getName()));

        int programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader.getId());
        GL20.glAttachShader(programId, fragmentShader.getId());
        GL20.glLinkProgram(programId);

        CompileResult compileResult = CompileStatusChecker.checkProgramCompile(programId);
        if (compileResult.isFailure())
            throw new CompileProgramException(String.format("Error compiling program '%s', reason: %s", name, compileResult.message()));

        return new GlProgram(name, programId, uniforms);
    }

    /*
     * Компилирует шейдер.
     */
    public static GlShader compileShader(String name, String content, ShaderType shaderType) {
        int shaderId = GL20.glCreateShader(shaderType.getId());
        GL20.glShaderSource(shaderId, includeShaderLibraries(content));
        GL20.glCompileShader(shaderId);

        CompileResult compileResult = CompileStatusChecker.checkShaderCompile(shaderId);
        if (compileResult.isFailure())
            throw new CompileShaderException(String.format("Error compiling shader '%s', reason: %s", name, compileResult.message()));

        return new GlShader(name, shaderId, shaderType);
    }

    /*
     * Внедряет в контент шейдера шейдерные библиотеки
     */
    public static String includeShaderLibraries(String content) {
        boolean writeAction = false;
        boolean writeLibName = false;
        StringBuilder s = new StringBuilder();
        int i = 0;
        while (i < content.length()){
            char ch = content.charAt(i);
            i++;

            if (ch == '#') {
                s = new StringBuilder("#");
                writeAction = true;
                continue;
            }
            if (ch == '<' && writeAction) {
                writeAction = false;
                if (s.toString().equals(includeLibAction)) {
                    writeLibName = true;
                }
                s = new StringBuilder();
                continue;
            }
            if (ch == '>' && writeLibName) {
                writeLibName = false;
                content = content.replace(includeLibAction.concat("<").concat(s.toString()).concat(">"), GlShaderLibraries.getLibrary(s.toString()).libraryContent());
                i -= "#".concat(includeLibAction).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }
        return content;
    }
}
