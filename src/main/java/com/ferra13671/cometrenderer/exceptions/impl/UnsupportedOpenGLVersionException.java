package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.exceptions.CometException;

public class UnsupportedOpenGLVersionException extends CometException {

    public UnsupportedOpenGLVersionException(GLVersion currentVersion, GLVersion minimumVersion) {
        super(String.format("OpenGL version (%s) does not match the minimum version (%s).", currentVersion.glVersion, minimumVersion.glVersion));
    }
}
