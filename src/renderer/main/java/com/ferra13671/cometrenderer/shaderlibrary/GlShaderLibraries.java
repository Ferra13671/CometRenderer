package com.ferra13671.cometrenderer.shaderlibrary;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchShaderLibraryException;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;

import java.util.HashMap;

/**
 * Класс, хранящий все шейдерные библиотеки, загруженные в CometRenderer.
 *
 * @deprecated хранение библиотек в будущем должно быть реализованно в самом компиляторе.
 *
 * @see GlShaderLibrary
 * @see GlobalCometCompiler
 */
//TODO перенести в GlobalCometCompiler
@Deprecated(forRemoval = true)
public final class GlShaderLibraries {
    private static final HashMap<String, GlShaderLibrary> libraries = new HashMap<>();

    /**
     * Добавляет шейдерные библиотеки в глобальный список.
     *
     * @param libraries шейдерные библиотеки.
     *
     * @see GlShaderLibrary
     */
    public static void addLibraries(GlShaderLibrary... libraries) {
        for (GlShaderLibrary library : libraries)
            GlShaderLibraries.libraries.put(library.libraryEntry().name(), library);
    }

    /**
     * Возвращает шейдерную библиотеку по её имени.
     * Если шейдерной библиотеки с данным именем не существует, то вызовется ошибка.
     *
     * @param name имя шейдерной библиотеки.
     * @return шейдерная библиотека.
     *
     * @see GlShaderLibrary
     */
    public static GlShaderLibrary getLibrary(String name) {
        GlShaderLibrary library = libraries.get(name);
        if (library == null)
            ExceptionPrinter.printAndExit(new NoSuchShaderLibraryException(name));
        return library;
    }
}
