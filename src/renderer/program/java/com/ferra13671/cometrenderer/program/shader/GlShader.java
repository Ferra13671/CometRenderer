package com.ferra13671.cometrenderer.program.shader;

import com.ferra13671.cometrenderer.builders.GlUniformSchema;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;

import java.util.List;

/**
 * Часть программы, исполняемая на GPU, которая отвечает за часть графической обработки программой: обработка вершин, растеризация, вычисление цвета пикселя и другие этапы рендеринга.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GlProgram
 * @see ShaderType
 * @see GlShaderLibrary
 * @see GlobalCometCompiler
 */
//TODO возможность получить контент шейдера
public class GlShader {
    /** Имя шейдера. **/
    private final String name;
    /** Айди шейдера в OpenGL. **/
    private final int id;
    /** Униформы, добавленные шейдером при интеграции в него шейдерных библиотек. **/
    private final List<GlUniformSchema<?>> extraUniforms;
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
    public GlShader(String name, int id, List<GlUniformSchema<?>> extraUniforms, ShaderType shaderType) {
        this.name = name;
        this.id = id;
        this.extraUniforms = extraUniforms;
        this.shaderType = shaderType;
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
    public List<GlUniformSchema<?>> getExtraUniforms() {
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
