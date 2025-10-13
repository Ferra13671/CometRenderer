package com.ferra13671.cometrenderer.shaderlibrary;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchShaderLibraryException;

import java.util.HashMap;

/*
 * Хранилище всех шейдерных библиотек в CometRenderer
 */
public final class GlShaderLibraries {
    private static final HashMap<String, GlShaderLibrary> libraries = new HashMap<>();

    /*
     * Добавляет библиотеки в список всех библиотек
     */
    public static void addLibraries(GlShaderLibrary... libraries) {
        for (GlShaderLibrary library : libraries)
            GlShaderLibraries.libraries.put(library.libraryEntry().name(), library);
    }

    /*
     * Возвращает библиотеку по её имени
     */
    public static GlShaderLibrary getLibrary(String name) {
        GlShaderLibrary library = libraries.get(name);
        if (library == null)
            ExceptionPrinter.printAndExit(new NoSuchShaderLibraryException(name));
        return library;
    }
}
