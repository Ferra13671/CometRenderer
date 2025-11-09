package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

/**
 * Ошибка, вызываемая в том случае, когда была произведена попытка добавить два шейдера с одинаковым типо в сборщик программы.
 */
public class DoubleShaderAdditionException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4075887372988299959L;

    public DoubleShaderAdditionException(String newShader, ShaderType type, String oldShader) {
        super(
                "Double shader addition in program builder.",
                String.format("An attempt was made to add shader '%s' with type '%s', while shader '%s' with the same type had already been added.", newShader, type.name(), oldShader),
                new String[]{
                        "Your program builder has an invalid structure.",
                        "You have added a program fragment to the program builder that already adds a shader of the required type."
                },
                new String[]{
                        "Check that your program builder structure is correct.",
                        "Check the program fragments you add to the program builder."
                }
        );
    }
}
