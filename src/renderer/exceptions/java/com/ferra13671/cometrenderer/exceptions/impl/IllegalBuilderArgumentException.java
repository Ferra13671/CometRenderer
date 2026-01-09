package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;
import lombok.Getter;

/**
 * Ошибка, вызываемая в том случае, когда в каком-нибудь сборщике обнаружены неверные данные.
 */
public class IllegalBuilderArgumentException extends CometException {
    @Getter
    private final String builder;

    public IllegalBuilderArgumentException(String builder, String message) {
        super(message);

        this.builder = builder;
    }
}
