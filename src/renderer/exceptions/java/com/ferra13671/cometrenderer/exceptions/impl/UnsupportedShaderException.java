package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда был встречен шейдер с типом, неподдерживаемым в текущей версии OpenGL
 */
public class UnsupportedShaderException extends CometException {

    public UnsupportedShaderException(ShaderType shaderType) {
        super(String.format("Your OpenGL context does not support shader type '%s'.", shaderType.name));
    }
}
