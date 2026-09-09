package com.ferra13671.cometrenderer.exceptions.impl.compile;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class CompileShaderException extends CometException {

    public CompileShaderException(String shaderName, String reason) {
        super(String.format("Error compiling shader '%s', reason:\n%s.", shaderName, reason));
    }
}
