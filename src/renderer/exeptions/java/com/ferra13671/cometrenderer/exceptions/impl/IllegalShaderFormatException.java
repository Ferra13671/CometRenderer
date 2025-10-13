package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/*
 * Это исключение вызывается, когда формат шейдера не соотвестствует нужному формату
 */
public class IllegalShaderFormatException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -1092141420001333865L;

    public IllegalShaderFormatException(String shaderName, String needShaderType) {
        super(
                "Shader format error.",
                String.format("'%s' is not a %s shader.", shaderName,needShaderType),
                new String[]{
                        "You pass two shaders of the same type to the program"
                },
                new String[]{
                        "Check if shaders are passed correctly for compilation"
                }
        );
    }
}
