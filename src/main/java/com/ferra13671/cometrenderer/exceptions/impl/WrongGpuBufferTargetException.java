package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class WrongGpuBufferTargetException extends CometException {

    public WrongGpuBufferTargetException(int givenTarget, int requiredTaget) {
        super(String.format("The received gpu buffer has target '%s', but '%s' was expected..", givenTarget, requiredTaget));
    }
}
