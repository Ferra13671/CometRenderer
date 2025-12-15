package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда не удалось найти нужную униформу в программе.
 */
public class NoSuchUniformException extends CometException {

    public NoSuchUniformException(String uniformName, String programName) {
        super(String.format("Cannot find uniform '%s' in program '%s'.", uniformName, programName));
    }
}
