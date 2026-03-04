package com.ferra13671.cometrenderer.plugins.bettercompiler.exceptions;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class NoSuchShaderLibraryException extends CometException {

    public NoSuchShaderLibraryException(String shaderLibName) {
        super(String.format("Cannot find shader library with name '%s'", shaderLibName));
    }
}
