package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalShaderTypeException;
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

/**
 * Глобальный компилятор CometRender'а, используемый для компиляции различных объектов.
 *
 * @see GlProgram
 * @see GlShader
 * @see GlslFileEntry
 * @see GlShaderLibrary
 */
public class GlobalCometCompiler {
    /**
     * Оператор вставки шейдерно библиотеки в шейдер.
     * Позволяет вставлять любуб имеющуюся шейдерную библиотеку в шейдер на стадии компиляции.
     * <p>
     * Что бы вставить библиотеку в шейдер нужно написать {@code #include<name>}, где {@code name} — имя шейдерной библиотеки.
     * Примером использования вставки шейдерной библиотеки может поступить данный шейдер:
     * <pre><code>
     *     #version 330 core
     *
     *     in vec3 position;
     *
     *     #include<matrixUniforms> //Начиная с этой строки будет вставлена шейдерная библиотека, которую вы загрузили в CometRender с именем "matrixUniforms"
     *
     *     void main() {
     *         gl_Position = projMat * modelViewMat * vec4(position, 1.);
     *     }
     * </code></pre>
     * <p>
     * Компилятор также может поддерживать рекурсию вставки шейдерных библиотек.
     * Т.е. вы можете вставить в шейдер библиотеку, которая вместе с собой вставит еще одну библиотеку, и так множество раз.
     * Главное не делать зацикленные библиотеки либо библиотеки, которые внедряют сами себя, иначе вы просто заставите компилятор внедрять шейдерные библиотеки в бесконечном цикле, а это плохо.
     */
    private static final String includeLibAction = "#include";

    /**
     * Компилирует программу из данных.
     *
     * @param name имя программы.
     * @param vertexShader вершинный шейдер.
     * @param fragmentShader фрагментный шейдер.
     * @param uniforms униформы программы.
     * @return скомпилированная программа.
     *
     * @see GlProgram
     * @see GlShader
     */
    //TODO возможность добавлять все типы шейдеров в программу
    public static GlProgram compileProgram(String name, GlShader vertexShader, GlShader fragmentShader, List<GlUniformSchema<?>> uniforms) {
        //Проверяем правильность типов шейдеров
        if (vertexShader.getShaderType() != ShaderType.Vertex)
            ExceptionPrinter.printAndExit(new IllegalShaderTypeException(vertexShader.getName(), "vertex"));
        if (fragmentShader.getShaderType() != ShaderType.Fragment)
            ExceptionPrinter.printAndExit(new IllegalShaderTypeException(fragmentShader.getName(), "fragment"));

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

    /**
     * Компилирует шейдер из данных.
     *
     * @param shaderEntry glsl контент шейдера.
     * @param shaderType тип шейдера.
     * @return скомпилированный шейдер.
     *
     * @see GlShader
     * @see ShaderType
     */
    public static GlShader compileShader(GlslFileEntry shaderEntry, ShaderType shaderType) {
        //Внедряем в контент шейдерные библиотеки, а так же получаем все юниформы, которые они добавляют
        ShaderContent content = includeShaderLibraries(shaderEntry.content());

        //Создаём шейдер в OpenGL
        int shaderId = GL20.glCreateShader(shaderType.glId);
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

    /**
     * Компилирует контент шейдера для последующего его применения.
     *
     * @param name имя контента.
     * @param contentGetter функция для получения контента из пути.
     * @param shaderPath путь к контенту.
     * @return контент шейдера.
     * @param <T> тип объекта, используемого как путь к контенту шейдеров.
     */
    //TODO убрать т.к. хуйня.
    public static <T> GlslFileEntry compileShaderEntry(String name, Function<T, String> contentGetter, T shaderPath) {
        return new GlslFileEntry(name, contentGetter.apply(shaderPath));
    }

    /**
     * Компилирует шейдерную библиотеку из данных.
     *
     * @param name имя шейдерной библиотеки.
     * @param contentGetter функция для получения контента из пути.
     * @param libraryPath путь к контенту.
     * @param uniforms униформы шейдерной библиотеки.
     * @return шейдерная библиотека.
     * @param <T> тип объекта, используемого как путь к контенту шейдеров.
     */
    //TODO убрать
    public static <T> GlShaderLibrary compileShaderLibrary(String name, Function<T, String> contentGetter, T libraryPath, List<GlUniformSchema<?>> uniforms) {
        return new GlShaderLibrary(
                new GlslFileEntry(
                        name,
                        contentGetter.apply(libraryPath)
                ),
                uniforms
        );
    }

    /**
     * Внедряет в контент шейдера нужные шейдерные библиотеки.
     *
     * @param content контент шейдера.
     * @return шейдерный контент с внедренными библиотеками и добавленными униформами.
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
