package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда произошла ошибка при компиляции программы.
 */
public class CompileProgramException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676940L;

    public CompileProgramException(String message) {
        super(message);
    }

    public CompileProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompileProgramException(Throwable cause) {
        super(cause);
    }
}
