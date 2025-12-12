package com.ferra13671.cometrenderer.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchShaderLibraryException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.program.GlProgram;
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
 */
/*
    TODO
        Ability to customize the compilation process using plugins
        Move the shader library system into a separate plugin
        'extra-compiler' plugin
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
    public static final String SHADER_LIBRARY_FILE = "SHADER_LIB";
    public static final String DEFAULT_FILE = "DEFAULT";
    /** Карта шейдерных библиотек по их именам. **/
    private static final HashMap<String, GlslFileEntry> libraries = new HashMap<>();

    /**
     * Регистрирует шейдерные библиотеку в компилятор CometRenderer'а.
     *
     * @param fileEntries шейдерные библиотеки, которые нужно зарегистрировать.
     */
    public static void registerShaderLibraries(GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_FILE))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_FILE));

            libraries.put(fileEntry.getName(), fileEntry);
        }
    }

    /**
     * Отменяет регистрацию шейдерных библиотек в компиляторе CometRender'а.
     *
     * @param fileEntries шейдерные библиотеки, которым нужно отменить регистрацию.
     */
    public static void unregisterShaderLibraries(GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_FILE))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_FILE));

            libraries.remove(fileEntry.getName());
        }
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
    //TODO return Optional
    public static GlslFileEntry getShaderLibrary(String name) {
        GlslFileEntry entry = libraries.get(name);
        if (entry == null)
            ExceptionPrinter.printAndExit(new NoSuchShaderLibraryException(name));
        return entry;
    }

    /**
     * Компилирует программу из данных.
     *
     * @param registry реестр со всей необходимой информацией.
     * @param shaders список шейдеров программы.
     * @return скомпилированная программа.
     *
     * @see GlProgram
     * @see GlShader
     */
    public static GlProgram compileProgram(Registry registry, List<GlShader> shaders) {
        String name = registry.get(CometTags.NAME).orElseThrow().getValue();

        //Создаем программу в OpenGL
        int programId = GL20.glCreateProgram();

        HashMap<String, UniformType<?>> allUniforms = new HashMap<>(registry.get(CometTags.UNIFORMS).orElseThrow().getValue());

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
        GlProgram program = new GlProgram(name, programId, new HashSet<>(Arrays.asList(registry.get(CometTags.SNIPPETS).orElseThrow().getValue())), allUniforms);

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
        Registry registry = includeShaderLibraries(shaderEntry.getContent());
        String content = registry.get(CometTags.CONTENT).orElseThrow().getValue();

        //Создаём шейдер в OpenGL
        int shaderId = GL20.glCreateShader(shaderType.glId);
        //Устанавливаем контент шейдеру
        GL20.glShaderSource(shaderId, content);
        //Компилируем шейдер
        GL20.glCompileShader(shaderId);

        //Создаём новый шейдер
        GlShader shader = new GlShader(shaderEntry.getName(), content, shaderId, registry.get(CometTags.UNIFORMS).orElseThrow().getValue(), shaderType);

        //Получаем результат компиляции
        CompileResult compileResult = shader.getCompileResult();

        //Выбрасываем ошибку, если нужно
        if (compileResult.isFailure())
            ExceptionPrinter.printAndExit(new CompileShaderException(shaderEntry.getName(), compileResult.message()));

        return shader;
    }

    /**
     * Внедряет в контент шейдера нужные шейдерные библиотеки.
     *
     * @param content контент шейдера.
     * @return шейдерный контент с внедренными библиотеками и добавленными униформами.
     */
    public static Registry includeShaderLibraries(String content) {
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

                GlslFileEntry library = getShaderLibrary(s.toString());

                library.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                    if (uniforms.containsKey(s1))
                        ExceptionPrinter.printAndExit(new DoubleUniformAdditionException(s1));

                    uniforms.put(s1, uniformType);
                });

                content = content.replace(includeLibOperator.concat("<").concat(s.toString()).concat(">"), library.getContent());
                i -= "#".concat(includeLibOperator).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }

        Registry registry = new Registry();
        registry.addImmutable(CometTags.CONTENT, content);
        registry.addImmutable(CometTags.UNIFORMS, uniforms);

        return registry;
    }
}