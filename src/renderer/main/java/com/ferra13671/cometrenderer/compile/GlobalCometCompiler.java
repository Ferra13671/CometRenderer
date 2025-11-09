package com.ferra13671.cometrenderer.compile;

import com.ferra13671.cometrenderer.Pair;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchShaderLibraryException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

import java.util.*;

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
    private static final String includeLibOperator = "#include";
    /** Карта шейдерных библиотек по их именам. **/
    private static final HashMap<String, GlShaderLibrary> libraries = new HashMap<>();

    /**
     * Регистрирует шейдерные библиотеку в компилятор CometRenderer'а.
     *
     * @param shaderLibraries шейдерные библиотеки, которые нужно зарегистрировать.
     */
    public static void registerShaderLibraries(GlShaderLibrary... shaderLibraries) {
        for (GlShaderLibrary shaderLibrary : shaderLibraries)
            libraries.put(shaderLibrary.libraryEntry().name(), shaderLibrary);
    }

    /**
     * Отменяет регистрацию шейдерных библиотек в компиляторе CometRender'а.
     *
     * @param shaderLibraries шейдерные библиотеки, которым нужно отменить регистрацию.
     */
    public static void unregisterShaderLibraries(GlShaderLibrary... shaderLibraries) {
        for (GlShaderLibrary shaderLibrary : shaderLibraries)
            libraries.remove(shaderLibrary.libraryEntry().name());
    }

    /**
     * Отменяет регистрацию шейдерных библиотек в компиляторе CometRender'а.
     *
     * @param names имена шейдерных библиотек, которым нужно отменить регистрацию.
     */
    public static void unregisterShaderLibraries(String... names) {
        for (String name : names)
            libraries.remove(name);
    }

    /**
     * Возвращает зарегистрированную шейдерную библиотеку по её имени.
     * Если в списке зарегистрированных шейдерных библиотек нужная не была найдена, то вызовется ошибка.
     *
     * @param name имя требуемой шейдерной библиотеки.
     * @return шейдерная библиотека.
     */
    public static GlShaderLibrary getShaderLibrary(String name) {
        GlShaderLibrary library = libraries.get(name);
        if (library == null)
            ExceptionPrinter.printAndExit(new NoSuchShaderLibraryException(name));
        return library;
    }

    /**
     * Компилирует программу из данных.
     *
     * @param name имя программы.
     * @param shaders список шейдеров программы.
     * @param snippets фрагменты программы, добавленные в программу.
     * @param uniforms униформы программы.
     * @return скомпилированная программа.
     *
     * @see GlProgram
     * @see GlShader
     */
    public static GlProgram compileProgram(String name, List<GlShader> shaders, GlProgramSnippet[] snippets, HashMap<String, UniformType<?>> uniforms) {
        //Создаем программу в OpenGL
        int programId = GL20.glCreateProgram();

        HashMap<String, UniformType<?>> allUniforms = new HashMap<>(uniforms);

        //Добавляем в программу все шейдеры
        for (GlShader shader : shaders) {
            GL20.glAttachShader(programId, shader.getId());

            shader.getExtraUniforms().forEach((s, uniformType) -> {
                if (allUniforms.containsKey(s))
                    ExceptionPrinter.printAndExit(new DoubleUniformAdditionException(s));

                allUniforms.put(s, uniformType);
            });
        }

        //Компилируем программу
        GL20.glLinkProgram(programId);

        //Создаём новую программу.
        GlProgram program = new GlProgram(name, programId, new HashSet<>(Arrays.asList(snippets)), allUniforms);

        //Получаем результат компиляции
        CompileResult compileResult = program.getCompileResult();

        //Выбрасываем ошибку, если нужно
        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileProgramException(name, compileResult.message()));

        return program;
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
        Pair<String, HashMap<String, UniformType<?>>> content = includeShaderLibraries(shaderEntry.content());

        //Создаём шейдер в OpenGL
        int shaderId = GL20.glCreateShader(shaderType.glId);
        //Устанавливаем контент шейдеру
        GL20.glShaderSource(shaderId, content.getLeft());
        //Компилируем шейдер
        GL20.glCompileShader(shaderId);

        //Создаём новый шейдер
        GlShader shader = new GlShader(shaderEntry.name(), content.getLeft(), shaderId, content.getRight(), shaderType);

        //Получаем результат компиляции
        CompileResult compileResult = shader.getCompileResult();

        //Выбрасываем ошибку, если нужно
        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileShaderException(shaderEntry.name(), compileResult.message()));

        return shader;
    }

    /**
     * Внедряет в контент шейдера нужные шейдерные библиотеки.
     *
     * @param content контент шейдера.
     * @return шейдерный контент с внедренными библиотеками и добавленными униформами.
     */
    public static Pair<String, HashMap<String, UniformType<?>>> includeShaderLibraries(String content) {
        HashMap<String, UniformType<?>> uniforms = new HashMap<>();

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
                if (s.toString().equals(includeLibOperator)) {
                    writeLibName = true;
                }
                s = new StringBuilder();
                continue;
            }
            if (ch == '>' && writeLibName) {
                writeLibName = false;

                GlShaderLibrary library = getShaderLibrary(s.toString());

                library.uniforms().forEach((s1, uniformType) -> {
                    if (uniforms.containsKey(s1))
                        ExceptionPrinter.printAndExit(new DoubleUniformAdditionException(s1));

                    uniforms.put(s1, uniformType);
                });

                content = content.replace(includeLibOperator.concat("<").concat(s.toString()).concat(">"), library.libraryEntry().content());
                i -= "#".concat(includeLibOperator).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }

        return new Pair<>(content, uniforms);
    }
}