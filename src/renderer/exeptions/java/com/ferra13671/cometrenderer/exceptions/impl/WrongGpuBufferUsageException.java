package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class WrongGpuBufferUsageException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -1616138098256794198L;

    public WrongGpuBufferUsageException(int givenUsage, int requiredUsage) {
        super(
                "Wrong gpu buffer usage.",
                String.format("The received gpu buffer has usage '%s', but '%s' was expected..", givenUsage, requiredUsage),
                new String[]{
                        "You are giving the method that caused the error an wrong gpu buffer"
                },
                new String[]{
                        "Recheck the method call that caused the error and fix the buffer issue"
                }
        );
    }
}
