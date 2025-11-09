package com.ferra13671.cometrenderer.program.shader;

import com.ferra13671.cometrenderer.Compilable;
import com.ferra13671.cometrenderer.compile.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.compile.GlShaderLibrary;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatus;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;
import java.util.HashMap;

/**
 * Часть программы, исполняемая на GPU, которая отвечает за часть графической обработки программой: обработка вершин, растеризация, вычисление цвета пикселя и другие этапы рендеринга.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GlProgram
 * @see ShaderType
 * @see GlShaderLibrary
 * @see GlobalCometCompiler
 */
public class GlShader implements Compilable, Closeable {
    /** Имя шейдера. **/
    private final String name;
    /** Контент шейдера. **/
    private final String content;
    /** Айди шейдера в OpenGL. **/
    private final int id;
    /** Униформы, добавленные шейдером при интеграции в него шейдерных библиотек. **/
    private final HashMap<String, UniformType<?>> extraUniforms;
    /** Тип шейдера. **/
    private final ShaderType shaderType;

    /**
     * @param name имя шейдера.
     * @param id айди шейдера в OpenGL.
     * @param extraUniforms униформы, добавленные шейдером при интеграции в него шейдерных библиотек.
     * @param shaderType тип шейдера.
     *
     * @see GlProgram
     * @see ShaderType
     * @see GlShaderLibrary
     */
    public GlShader(String name, String content, int id, HashMap<String, UniformType<?>> extraUniforms, ShaderType shaderType) {
        this.name = name;
        this.content = content;
        this.id = id;
        this.extraUniforms = extraUniforms;
        this.shaderType = shaderType;
    }

    @Override
    @OverriddenMethod
    public void close() {
        GL20.glDeleteShader(getId());
        this.extraUniforms.clear();
    }

    @Override
    @OverriddenMethod
    public CompileResult getCompileResult() {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetShaderi(getId(), GL20.GL_COMPILE_STATUS));
        return new CompileResult(
                status,
                status == CompileStatus.FAILURE ? StringUtils.trim(GL20.glGetShaderInfoLog(getId())) : ""
        );
    }

    /**
     * Возвращает имя шейдера.
     *
     * @return имя шейдера.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает контент шейдера.
     *
     * @return контент шейдера.
     */
    public String getContent() {
        return content;
    }

    /**
     * Возвращает айди шейдера в OpenGL.
     *
     * @return айди шейдера в OpenGL.
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает униформы, добавленные шейдером при интеграции в него шейдерных библиотек.
     *
     * @return униформы, добавленные шейдером при интеграции в него шейдерных библиотек.
     * @see GlShaderLibrary
     */
    public HashMap<String, UniformType<?>> getExtraUniforms() {
        return extraUniforms;
    }

    /**
     * Возвращает тип шейдера.
     *
     * @return тип шейдера.
     * @see ShaderType
     */
    public ShaderType getShaderType() {
        return shaderType;
    }
}
