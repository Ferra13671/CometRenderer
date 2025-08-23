package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда формат шейдера не соотвестствует нужному формату
 */
public class IllegalShaderFormatException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676936L;

    public IllegalShaderFormatException(String message) {
        super(message);
    }

    public IllegalShaderFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalShaderFormatException(Throwable cause) {
        super(cause);
    }
}
