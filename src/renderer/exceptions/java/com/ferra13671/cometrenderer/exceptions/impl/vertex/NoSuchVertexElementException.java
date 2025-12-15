package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда не удалось получить нужный элемент вершины в формате вершины.
 */
public class NoSuchVertexElementException extends CometException {

    public NoSuchVertexElementException(String elementName) {
        super(String.format("Cannot find vertex element '%s'.", elementName));
    }
}
