package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике меша количество вершин превысило максимальное допустимое значение.
 */
public class VertexOverflowException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 2211120929779008363L;

    public VertexOverflowException() {
        super(
                "VertexBuilder overflow.",
                String.format("The number of vertices in VertexBuilder exceeded the maximum value (%s).", MeshBuilder.MAX_VERTICES),
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
