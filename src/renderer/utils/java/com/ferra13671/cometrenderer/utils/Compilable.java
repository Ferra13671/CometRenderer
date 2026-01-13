package com.ferra13671.cometrenderer.utils;

import com.ferra13671.cometrenderer.utils.compile.CompileResult;

/**
 * Интерфейс, реализующий возможность получить результат компиляции объекта, расширяющего данный интерфейс.
 *
 * @see CompileResult
 */
public interface Compilable {

    /**
     * Возвращает результат компиляции объекта, расширяющего данный интерфейс.
     *
     * @return результат компиляции объекта.
     *
     * @see CompileResult
     */
    CompileResult getCompileResult();
}
