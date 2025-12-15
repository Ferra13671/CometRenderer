package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

/**
 * Ошибка, вызываемая в том случае, когда была произведена попытка добавить два шейдера с одинаковым типо в сборщик программы.
 */
public class DoubleShaderAdditionException extends CometException {

    public DoubleShaderAdditionException(String newShader, ShaderType type, String oldShader) {
        super(String.format("An attempt was made to add shader '%s' with type '%s', while shader '%s' with the same type had already been added.", newShader, type.name(), oldShader));
    }
}
