package com.ferra13671.cometrenderer.program.shader;

import com.ferra13671.cometrenderer.exceptions.CompileShaderException;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatusChecker;
import org.lwjgl.opengl.GL20;

/*
 * Шейдер — часть программы, выполняющая различные функции при отрисовке пискелей.
 */
public class Shader {
    private final String name;
    private final int id;
    private final ShaderType shaderType;

    private Shader(String name, int id, ShaderType shaderType) {
        this.name = name;
        this.id = id;
        this.shaderType = shaderType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ShaderType getShaderType() {
        return shaderType;
    }

    /*
     * Компилирует шейдер.
     */
    public static Shader compile(String name, String content, ShaderType shaderType) {
        int shaderId = GL20.glCreateShader(shaderType.getId());
        GL20.glShaderSource(shaderId, content);
        GL20.glCompileShader(shaderId);

        CompileResult compileResult = CompileStatusChecker.checkShaderCompile(shaderId);
        if (compileResult.isFailure())
            throw new CompileShaderException(String.format("Error compiling shader '%s', reason: %s", name, compileResult.message()));

        return new Shader(name, shaderId, shaderType);
    }
}
