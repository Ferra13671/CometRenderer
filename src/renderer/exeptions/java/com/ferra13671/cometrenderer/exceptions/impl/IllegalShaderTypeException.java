package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда тип шейдера не соответствует нужному типу.
 */
public class IllegalShaderTypeException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -1092141420001333865L;

    public IllegalShaderTypeException(String shaderName, String needShaderType) {
        super(
                "Shader type error.",
                String.format("'%s' is not a %s shader.", shaderName,needShaderType),
                new String[]{
                        "You pass two shaders of the same type to the program",
                        "You have assigned the wrong type of shader to the program."
                },
                new String[]{
                        "Check if shaders are passed correctly for compilation"
                }
        );
    }
}
