package com.ferra13671.cometrenderer.exceptions.impl.compile;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class CompileProgramException extends CometException {

    public CompileProgramException(String programName, String reason) {
        super(String.format("Error compiling program '%s', reason:\n%s.", programName, reason));
    }
}
