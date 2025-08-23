package com.ferra13671.cometrenderer.exceptions;

/*
 * Это исключение вызывается, когда произошла ошибка при компиляции шейдера (вертексного либо фрагментного)
 */
public class CompileShaderException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -7031024790190676939L;

    public CompileShaderException(String message) {
        super(message);
    }

    public CompileShaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompileShaderException(Throwable cause) {
        super(cause);
    }
}
