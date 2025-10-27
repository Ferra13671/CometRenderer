package com.ferra13671.cometrenderer.program.shader;

import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/*
 * Шейдер — часть программы, выполняющая различные функции при отрисовке пискелей.
 */
public class GlShader {
    private final String name;
    private final int id;
    private final List<GlUniformSchema<?>> extraUniforms;
    private final ShaderType shaderType;

    public GlShader(String name, int id, List<GlUniformSchema<?>> extraUniforms, ShaderType shaderType) {
        this.name = name;
        this.id = id;
        this.extraUniforms = extraUniforms;
        this.shaderType = shaderType;
    }

    /*
     * Возвращает имя шейдера
     */
    public String getName() {
        return name;
    }

    /*
     * Возвращает айди шейдера в OpenGL
     */
    public int getId() {
        return id;
    }

    /*
     * Возвращает дополнительные униформы, которые были добавлены шейдерными библиотеками
     */
    public List<GlUniformSchema<?>> getExtraUniforms() {
        return extraUniforms;
    }

    /*
     * Возвращает тип шейдера
     */
    public ShaderType getShaderType() {
        return shaderType;
    }
}
