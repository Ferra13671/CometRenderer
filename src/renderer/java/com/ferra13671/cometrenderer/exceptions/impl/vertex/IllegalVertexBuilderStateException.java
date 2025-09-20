package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class IllegalVertexBuilderStateException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 6447527243741543095L;

    public IllegalVertexBuilderStateException(String details, String[] reasons, String[] solutions) {
        super(
                "Illegal VertexBuilder state.",
                details,
                reasons,
                solutions
        );
    }
}
