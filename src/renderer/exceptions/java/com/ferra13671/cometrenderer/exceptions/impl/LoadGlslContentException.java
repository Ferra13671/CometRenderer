package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при загрузке glsl контента произошла ошибка.
 */
public class LoadGlslContentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4206273236678422364L;

    public LoadGlslContentException(Exception e) {
        super("Cannot load glsl content. Reason: ".concat(e.getMessage()));
    }
}
