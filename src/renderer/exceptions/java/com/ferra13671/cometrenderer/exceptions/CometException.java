package com.ferra13671.cometrenderer.exceptions;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.3")
public class CometException extends RuntimeException {

    public CometException(String message) {
        super(message);
    }
}
