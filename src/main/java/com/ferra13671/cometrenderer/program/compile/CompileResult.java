package com.ferra13671.cometrenderer.program.compile;

/*
 * Объект, хранящий результат компиляции шейдера, либо программы
 */
public record CompileResult(int status, String message) {

    public boolean isFailure() {
        return status == 0;
    }
}
