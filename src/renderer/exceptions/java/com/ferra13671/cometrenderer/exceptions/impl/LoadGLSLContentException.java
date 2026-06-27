package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class LoadGLSLContentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4206273236678422364L;

    public LoadGLSLContentException(Exception e) {
        super("Cannot load GLSL content. Reason: ".concat(e.getMessage()));
    }
}
