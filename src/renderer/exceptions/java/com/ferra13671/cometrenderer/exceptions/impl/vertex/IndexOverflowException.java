package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class IndexOverflowException extends CometException {

    public IndexOverflowException(int maxIndices) {
        super(String.format("The number of indices exceeded the maximum value (%s).", maxIndices));
    }
}
