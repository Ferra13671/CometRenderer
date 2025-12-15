package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при попытке закончить сборку вершины вершина имеет неверную структуру.
 */
public class BadVertexStructureException extends CometException {

    public BadVertexStructureException(String missingElements) {
        super("Missing elements in vertex: " + missingElements);
    }
}
