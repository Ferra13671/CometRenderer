package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/*
 * Это исключение вызывается, когда сборщик программы имеет неверные данные
 */
public class IllegalProgramBuilderArgumentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = 6021165256752908695L;

    public IllegalProgramBuilderArgumentException(String message) {
        super(
                "Illegal argument in program builder..",
                message,
                new String[]{
                        "You did not specify all the required arguments before building program builder",
                        "The argument you specified in program builder is null"
                },
                new String[]{
                        "Check if you are specifying all arguments when building library builder.",
                        "Check if you are not passing any arguments that are null."
                }
        );
    }
}
