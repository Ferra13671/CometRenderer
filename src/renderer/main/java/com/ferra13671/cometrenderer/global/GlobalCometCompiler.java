package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalShaderFormatException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.builder.GlUniformSchema;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatusChecker;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibraries;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;
import org.lwjgl.opengl.GL20;

import java.util.List;
import java.util.function.Function;

/*
 * Глобальный компилятор
 */
public class GlobalCometCompiler {
    private static final String includeLibAction = "#include";

    /*
     * Компилирует программу
     */
    public static GlProgram compileProgram(String name, GlShader vertexShader, GlShader fragmentShader, List<GlUniformSchema<?>> uniforms) {
        if (vertexShader.getShaderType() != ShaderType.Vertex)
            ExceptionPrinter.printAndExit(new IllegalShaderFormatException(vertexShader.getName(), "vertex"));
        if (fragmentShader.getShaderType() != ShaderType.Fragment)
            ExceptionPrinter.printAndExit(new IllegalShaderFormatException(fragmentShader.getName(), "fragment"));

        int programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader.getId());
        GL20.glAttachShader(programId, fragmentShader.getId());
        GL20.glLinkProgram(programId);

        CompileResult compileResult = CompileStatusChecker.checkProgramCompile(programId);

        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileProgramException(name, compileResult.message()));

        return new GlProgram(name, programId, uniforms);
    }

    /*
     * Компилирует шейдер.
     */
    public static GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType) {
        int shaderId = GL20.glCreateShader(shaderType.getId());
        GL20.glShaderSource(shaderId, shaderEntry.content());
        GL20.glCompileShader(shaderId);

        CompileResult compileResult = CompileStatusChecker.checkShaderCompile(shaderId);

        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileShaderException(shaderEntry.name(), compileResult.message()));

        return new GlShader(shaderEntry.name(), shaderId, shaderType);
    }

    /*
     * Компилирует данные шейдера для последующего их применения
     */
    public static <T> GlslFileEntry compileShaderEntry(String name, Function<T, String> contentGetter, T shaderPath) {
        //Внедряем в контент шейдера необходимые библиотеки
        String content = includeShaderLibraries(contentGetter.apply(shaderPath));

        return new GlslFileEntry(name, content);
    }

    /*
     * Компилирует шейдерную библиотеку при помощи её данных
     */
    public static <T> GlShaderLibrary compileShaderLibrary(String name, Function<T, String> contentGetter, T libraryPath, List<GlUniformSchema<?>> uniforms) {
        return new GlShaderLibrary(
                new GlslFileEntry(
                        name,
                        contentGetter.apply(libraryPath)
                ),
                uniforms
        );
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
                content = content.replace(includeLibAction.concat("<").concat(s.toString()).concat(">"), GlShaderLibraries.getLibrary(s.toString()).libraryEntry().content());
                i -= "#".concat(includeLibAction).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }
        return content;
    }
}
