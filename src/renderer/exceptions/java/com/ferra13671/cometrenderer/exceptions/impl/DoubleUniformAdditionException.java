package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда была произведена попытка добавить две униформы с одинаковым именем в сборщик программы.
 */
public class DoubleUniformAdditionException extends CometException {

    public DoubleUniformAdditionException(String uniformName) {
        super(String.format("An attempt was made to add a uniform named '%s' that already exists.", uniformName));
    }
}
