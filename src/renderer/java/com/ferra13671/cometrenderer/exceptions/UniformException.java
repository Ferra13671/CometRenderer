package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда произошла ошибка при работе с юниформом программы.
 */
public class UniformException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676938L;

    public UniformException(String message) {
        super(message);
    }

    public UniformException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniformException(Throwable cause) {
        super(cause);
    }
}
