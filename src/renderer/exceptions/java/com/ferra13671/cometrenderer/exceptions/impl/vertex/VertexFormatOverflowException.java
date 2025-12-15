package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда количество добавленных элементов в формат вышло за пределы максимального количества.
 */
public class VertexFormatOverflowException extends CometException {

    public VertexFormatOverflowException(int maxElements) {
        super(String.format("Amount of elements in VertexFormat cannot be greater than the maximum allowed amount ('%s').", maxElements));
    }
}
