package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда получение контента шейдера через его путь произошло с ошибкой
 */
public class LoadShaderContentException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676935L;

    public LoadShaderContentException(String message) {
        super(message);
    }

    public LoadShaderContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadShaderContentException(Throwable cause) {
        super(cause);
    }
}
