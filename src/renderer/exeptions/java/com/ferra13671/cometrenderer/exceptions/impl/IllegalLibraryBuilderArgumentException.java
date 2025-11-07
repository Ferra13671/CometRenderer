package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике шейдерной библиотеки встретились неверные данные.
 */
public class IllegalLibraryBuilderArgumentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = 577228207931063158L;

    public IllegalLibraryBuilderArgumentException(String message) {
        super(
                "Illegal argument in library builder.",
                message,
                new String[]{
                        "You did not specify all the required arguments before building library builder",
                        "The argument you specified in library builder is null"
                },
                new String[]{
                        "Check if you are specifying all arguments when building library builder.",
                        "Check if you are not passing any arguments that are null."
                }
        );
    }
}
