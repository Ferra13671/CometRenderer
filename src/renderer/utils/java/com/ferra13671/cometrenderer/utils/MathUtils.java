package com.ferra13671.cometrenderer.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public int roundUpToMultiple(int value, int divisor) {
        return ceilDiv(value, divisor) * divisor;
    }

    public int ceilDiv(int a, int b) {
        return -Math.floorDiv(-a, b);
    }
}
