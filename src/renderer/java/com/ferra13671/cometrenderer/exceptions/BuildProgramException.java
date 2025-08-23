package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда сборщик программы имеет неверные данные
 */
public class BuildProgramException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676937L;

    public BuildProgramException(String message) {
        super(message);
    }

    public BuildProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildProgramException(Throwable cause) {
        super(cause);
    }
}
