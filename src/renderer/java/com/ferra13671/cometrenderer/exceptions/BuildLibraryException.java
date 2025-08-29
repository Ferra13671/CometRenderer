package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда сборщик библиотеки имеет неверные данные
 */
public class BuildLibraryException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676934L;

    public BuildLibraryException(String message) {
        super(message);
    }

    public BuildLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildLibraryException(Throwable cause) {
        super(cause);
    }
}
