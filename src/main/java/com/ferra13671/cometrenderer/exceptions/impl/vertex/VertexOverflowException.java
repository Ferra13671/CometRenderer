package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class VertexOverflowException extends CometException {

    public VertexOverflowException(int maxVertices) {
        super(String.format("The number of vertices in MeshBuilder exceeded the maximum value (%s).", maxVertices));
    }
}
