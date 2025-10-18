package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class IllegalProgramPassBuilderArgumentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = 4302183795332301227L;

    public IllegalProgramPassBuilderArgumentException(String message) {
        super(
                "Illegal argument in program pipeline builder.",
                message,
                new String[]{
                        "You did not specify all the required arguments before building program pipeline builder",
                        "The argument you specified in program pipeline builder is null"
                },
                new String[]{
                        "Check if you are specifying all arguments when building program pipeline builder.",
                        "Check if you are not passing any arguments that are null."
                }
        );
    }
}
