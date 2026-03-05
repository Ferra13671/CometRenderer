package com.ferra13671.cometrenderer.utils;

import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import org.apiguardian.api.API;

/**
 * Интерфейс, реализующий возможность получить результат компиляции объекта, расширяющего данный интерфейс.
 *
 * @see CompileResult
 */
@API(status = API.Status.INTERNAL)
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
