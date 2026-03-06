package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда не удалось найти нужное расширение компилятора
 */
public class NoSuchCompilerExtensionException extends CometException {

    public NoSuchCompilerExtensionException(String extName) {
        super(String.format("Cannot find compiler extension with name '%s'.", extName));
    }
}
