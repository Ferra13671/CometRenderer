package com.ferra13671.cometrenderer.exceptions.impl.compile;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда произошла ошибка при компиляции шейдера.
 */
public class CompileShaderException extends CometException {

    public CompileShaderException(String shaderName, String reason) {
        super(String.format("Error compiling shader '%s', reason:\n%s.", shaderName, reason));
    }
}
