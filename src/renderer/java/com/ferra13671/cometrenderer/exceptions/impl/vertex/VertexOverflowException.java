package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/*
 * Это исключение вызывается, когда в VertexBuilder количество вершин превысило максимальное количество (16777215)
 */
public class VertexOverflowException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 2211120929779008363L;

    public VertexOverflowException() {
        super(
                "VertexBuilder overflow.",
                "The number of vertices in VertexBuilder exceeded the maximum value (16777215).",
                new String[]{
                        "You may be adding vertices in an infinite loop.",
                        "You're a monster who managed to manually exceed the maximum number of vertices."
                },
                new String[]{
                        "Check the method where you add vertices to VertexBuilder and fix it."
                }
        );
    }
}
