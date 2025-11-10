package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда была произведена попытка добавить две униформы с одинаковым именем в сборщик программы.
 */
public class DoubleUniformAdditionException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4075887372988299959L;

    public DoubleUniformAdditionException(String uniformName) {
        super(
                "Double uniform addition in program builder.",
                String.format("An attempt was made to add a uniform named '%s' that already exists.", uniformName),
                new String[]{
                        "Your program builder has an invalid structure.",
                        "You have added a program snippet to the program builder that already adds a uniform of the required name.",
                        "When the shader library was included in shader, a uniform with this name was already added."
                },
                new String[]{
                        "Check that your program builder structure is correct.",
                        "Check the program snippets you add to the program builder.",
                        "Check which shader libraries are being included into shaders and fix problem with uniforms."
                }
        );
    }
}
