package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при использовании GPU буффера его цель использования не совпадает с требуемой.
 */
public class WrongGpuBufferTargetException extends CometException {

    public WrongGpuBufferTargetException(int givenTarget, int requiredTaget) {
        super(String.format("The received gpu buffer has target '%s', but '%s' was expected..", givenTarget, requiredTaget));
    }
}
