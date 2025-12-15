package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда сборщик меша имеет неверное состояние.
 */
public class IllegalMeshBuilderStateException extends CometException {
    private final String details;
    private final String[] reasons;
    private final String[] solutions;

    public IllegalMeshBuilderStateException(String details, String[] reasons, String[] solutions) {
        super(details);

        this.details = details;
        this.reasons = reasons;
        this.solutions = solutions;
    }

    public String getDetails() {
        return details;
    }

    public String[] getReasons() {
        return reasons;
    }

    public String[] getSolutions() {
        return solutions;
    }
}
