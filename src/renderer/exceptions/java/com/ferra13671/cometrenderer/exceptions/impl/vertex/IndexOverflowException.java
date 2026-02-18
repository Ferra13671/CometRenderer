package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при генерации буффера индексов требуемое количество индексов превысило максимальное допустимое значение.
 */
public class IndexOverflowException extends CometException {

    public IndexOverflowException() {
        super(String.format("The number of indices exceeded the maximum value (%s).", CometRenderer.getConfig().MAX_INDICES.getValue()));
    }
}
