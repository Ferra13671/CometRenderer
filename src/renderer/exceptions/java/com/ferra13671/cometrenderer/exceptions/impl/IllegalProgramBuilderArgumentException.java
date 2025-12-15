package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда в сборщике программы встретились неверные данные.
 */
public class IllegalProgramBuilderArgumentException extends CometException {

    public IllegalProgramBuilderArgumentException(String message) {
        super(message);
    }
}
