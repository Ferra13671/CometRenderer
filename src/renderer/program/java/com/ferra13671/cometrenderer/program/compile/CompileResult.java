package com.ferra13671.cometrenderer.program.compile;

/*
 * Объект, хранящий результат компиляции шейдера, либо программы
 */
public record CompileResult(CompileStatus status, String message) {

    /*
     * Возвращает то, произошла ли ошибка при компиляции или нет
     */
    public boolean isFailure() {
        return status.equals(CompileStatus.FAILURE);
    }
}
