package com.ferra13671.cometrenderer.program.shader;

/*
 * Шейдер — часть программы, выполняющая различные функции при отрисовке пискелей.
 */
public class GlShader {
    private final String name;
    private final int id;
    private final ShaderType shaderType;

    public GlShader(String name, int id, ShaderType shaderType) {
        this.name = name;
        this.id = id;
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
     * Возвращает тип шейдера
     */
    public ShaderType getShaderType() {
        return shaderType;
    }
}
