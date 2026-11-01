package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.1")
public class WrongVertexFormatTypeException extends CometException {

    public WrongVertexFormatTypeException(String givenType, String requiredType) {
        super(String.format("The received vertex format has target '%s', but '%s' was expected.", givenType, requiredType));
    }
}
