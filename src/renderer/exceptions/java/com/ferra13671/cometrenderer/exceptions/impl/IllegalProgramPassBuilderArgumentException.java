package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике пасса пост эффекта встретились неверные данные.
 */
public class IllegalProgramPassBuilderArgumentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = 4302183795332301227L;

    public IllegalProgramPassBuilderArgumentException(String message) {
        super(
                "Illegal argument in program pass builder.",
                message,
                new String[]{
                        "You did not specify all the required arguments before building program pass builder",
                        "The argument you specified in program pass builder is null"
                },
                new String[]{
                        "Check if you are specifying all arguments when building program pass builder.",
                        "Check if you are not passing any arguments that are null."
                }
        );
    }
}
