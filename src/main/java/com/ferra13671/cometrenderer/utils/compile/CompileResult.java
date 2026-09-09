package com.ferra13671.cometrenderer.utils.compile;

import org.apiguardian.api.API;

/**
 * Объект, представляющий себя как результат компиляции чего-либо.
 *
 * @param status статус компиляции.
 * @param message сообщение ошибки, если компиляция произошла с ошибками.
 *
 * @see CompileStatus
 */
@API(status = API.Status.INTERNAL)
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
