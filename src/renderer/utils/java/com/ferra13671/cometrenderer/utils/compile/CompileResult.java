package com.ferra13671.cometrenderer.utils.compile;

/**
 * Объект, представляющий себя как результат компиляции чего-либо.
 *
 * @param status статус компиляции.
 * @param message сообщение ошибки, если компиляция произошла с ошибками.
 *
 * @see CompileStatus
 */
public record CompileResult(CompileStatus status, String message) {

    /**
     * Возвращает то, произошла ли ошибка при компиляции или нет.
     *
     * @return произошла ли ошибка при компиляции или нет.
     */
    public boolean isFailure() {
        return status.equals(CompileStatus.FAILURE);
    }

}
