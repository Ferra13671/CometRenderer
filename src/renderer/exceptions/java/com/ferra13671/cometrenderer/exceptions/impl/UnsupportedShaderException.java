package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда был встречен шейдер с типом, неподдерживаемым в текущей версии OpenGL
 */
public class UnsupportedShaderException extends CometException {

    public UnsupportedShaderException(GLVersion currentVersion, GLVersion requiredVersion) {
        super(String.format("Current OpenGL version ('%s') does not match the minimum OpenGL version for the shader ('%s').", currentVersion.glVersion, requiredVersion.glVersion));
    }
}
