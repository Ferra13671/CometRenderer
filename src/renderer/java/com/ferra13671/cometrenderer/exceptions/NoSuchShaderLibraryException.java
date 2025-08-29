package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда в загруженных шейдерных библиотеках не была найдена нужная
 */
public class NoSuchShaderLibraryException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676932L;

    public NoSuchShaderLibraryException(String message) {
        super(message);
    }

    public NoSuchShaderLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchShaderLibraryException(Throwable cause) {
        super(cause);
    }
}
