package com.ferra13671.cometrenderer.exceptions.impl.compile;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда произошла ошибка при компиляции программы.
 */
public class CompileProgramException extends CometException {

    public CompileProgramException(String programName, String reason) {
        super(String.format("Error compiling program '%s', reason:\n%s.", programName, reason));
    }
}
