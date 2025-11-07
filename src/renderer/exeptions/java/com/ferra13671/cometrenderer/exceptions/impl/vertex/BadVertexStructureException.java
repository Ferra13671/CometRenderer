package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при попытке закончить сборку вершины вершина имеет неверную структуру.
 */
public class BadVertexStructureException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 8335265683030650497L;

    public BadVertexStructureException(String missingElements) {
        super(
                "Bad vertex structure.",
                "Missing elements in vertex: " + missingElements,
                new String[]{
                        "When building vertex in VertexBuilder, you missed one or more elements."
                },
                new String[]{
                        "Check your vertex building method and fix it."
                }
        );
    }
}
