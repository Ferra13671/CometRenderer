package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда сборщик меша имеет неверное состояние.
 */
public class IllegalMeshBuilderStateException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 6447527243741543095L;

    public IllegalMeshBuilderStateException(String details, String[] reasons, String[] solutions) {
        super(
                "Illegal MeshBuilder state.",
                details,
                reasons,
                solutions
        );
    }
}
