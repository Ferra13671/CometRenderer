package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда получение контента библиотеки через ей путь произошло с ошибкой
 */
public class LoadLibraryContentException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676933L;

    public LoadLibraryContentException(String message) {
        super(message);
    }

    public LoadLibraryContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadLibraryContentException(Throwable cause) {
        super(cause);
    }
}
