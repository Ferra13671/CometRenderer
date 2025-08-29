package com.ferra13671.cometrenderer.shaderlibrary;

import com.ferra13671.cometrenderer.exceptions.NoSuchShaderLibraryException;

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
            GlShaderLibraries.libraries.put(library.name(), library);
    }

    /*
     * Возвращает библиотеку по её имени
     */
    public static GlShaderLibrary getLibrary(String name) {
        GlShaderLibrary library = libraries.get(name);
        if (library == null)
            throw new NoSuchShaderLibraryException(String.format("Cannot find shader library '%s' in global library list.", name));
        return library;
    }
}
