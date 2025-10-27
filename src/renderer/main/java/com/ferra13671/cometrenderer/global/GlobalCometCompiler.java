package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalShaderFormatException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatusChecker;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibraries;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
 * Глобальный компилятор
 */
public class GlobalCometCompiler {
    //Оператор вставки шейдерной библиотеки
    private static final String includeLibAction = "#include";

    /*
     * Компилирует программу
     */
    public static GlProgram compileProgram(String name, GlShader vertexShader, GlShader fragmentShader, List<GlUniformSchema<?>> uniforms) {
        //Проверяем правильность типов шейдеров
        if (vertexShader.getShaderType() != ShaderType.Vertex)
            ExceptionPrinter.printAndExit(new IllegalShaderFormatException(vertexShader.getName(), "vertex"));
        if (fragmentShader.getShaderType() != ShaderType.Fragment)
            ExceptionPrinter.printAndExit(new IllegalShaderFormatException(fragmentShader.getName(), "fragment"));

        //Создаем программу в OpenGL
        int programId = GL20.glCreateProgram();
        //Привязывает вертексный шейдер
        GL20.glAttachShader(programId, vertexShader.getId());
        //Привязываем фрайментный шейдер
        GL20.glAttachShader(programId, fragmentShader.getId());
        //Компилируем программу
        GL20.glLinkProgram(programId);

        //Получаем результат компиляции
        CompileResult compileResult = CompileStatusChecker.checkProgramCompile(programId);

        //Выбрасываем ошибку, если нужно
        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileProgramException(name, compileResult.message()));

        List<GlUniformSchema<?>> allUniforms = new ArrayList<>(uniforms);
        allUniforms.addAll(vertexShader.getExtraUniforms());
        allUniforms.addAll(fragmentShader.getExtraUniforms());

        //Возвращаем класс программы
        return new GlProgram(name, programId, allUniforms);
    }

    /*
     * Компилирует шейдер.
     */
    public static GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType) {
        //Внедряем в контент шейдерные библиотеки, а так же получаем все юниформы, которые они добавляют
        ShaderContent content = includeShaderLibraries(shaderEntry.content());

        //Создаём шейдер в OpenGL
        int shaderId = GL20.glCreateShader(shaderType.getId());
        //Устанавливаем контент шейдеру
        GL20.glShaderSource(shaderId, content.content());
        //Компилируем шейдер
        GL20.glCompileShader(shaderId);

        //Получаем результат компиляции
        CompileResult compileResult = CompileStatusChecker.checkShaderCompile(shaderId);

        //Выбрасываем ошибку, если нужно
        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileShaderException(shaderEntry.name(), compileResult.message()));

        //Возвращаем класс программы
        return new GlShader(shaderEntry.name(), shaderId, content.uniforms(), shaderType);
    }

    /*
     * Компилирует данные шейдера для последующего их применения
     */
    public static <T> GlslFileEntry compileShaderEntry(String name, Function<T, String> contentGetter, T shaderPath) {
        return new GlslFileEntry(name, contentGetter.apply(shaderPath));
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
    public static ShaderContent includeShaderLibraries(String content) {
        List<GlUniformSchema<?>> uniforms = new ArrayList<>();

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

                GlShaderLibrary library = GlShaderLibraries.getLibrary(s.toString());
                uniforms.addAll(library.uniforms());

                content = content.replace(includeLibAction.concat("<").concat(s.toString()).concat(">"), library.libraryEntry().content());
                i -= "#".concat(includeLibAction).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }

        return new ShaderContent(content, uniforms);
    }
}
