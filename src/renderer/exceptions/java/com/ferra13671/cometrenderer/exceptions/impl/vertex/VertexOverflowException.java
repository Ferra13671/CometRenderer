package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике меша количество вершин превысило максимальное допустимое значение.
 */
public class VertexOverflowException extends CometException {

    public VertexOverflowException() {
        super(String.format("The number of vertices in VertexBuilder exceeded the maximum value (%s).", MeshBuilder.MAX_VERTICES));
    }
}
