package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда не удалось найти нужную шейдерную библиотеку в списке загруженных.
 */
public class NoSuchShaderLibraryException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = 7949722130111886738L;

    public NoSuchShaderLibraryException(String libraryName) {
        super(
                "No such shader library error.",
                String.format("Cannot find shader library '%s' in global library list.", libraryName),
                new String[]{
                        "Incorrect library name",
                        "The library has not been added to global list"
                },
                new String[]{
                        "Make sure you entered the correct library name in shader",
                        "Make sure you add required library to global list before compiling shader"
                }
        );
    }
}
