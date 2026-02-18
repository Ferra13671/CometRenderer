package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике меша количество вершин превысило максимальное допустимое значение.
 */
public class VertexOverflowException extends CometException {

    public VertexOverflowException() {
        super(String.format("The number of vertices in MeshBuilder exceeded the maximum value (%s).", CometRenderer.getConfig().MAX_VERTICES.getValue()));
    }
}
