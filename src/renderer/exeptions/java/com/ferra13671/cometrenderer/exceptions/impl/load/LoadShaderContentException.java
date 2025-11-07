package com.ferra13671.cometrenderer.exceptions.impl.load;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при загрузке контента шейдера произошла ошибка.
 */
//TODO переименовать в LoadGlslContentException
public class LoadShaderContentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4206273236678422364L;

    public LoadShaderContentException(Exception e) {
        super(
                "Load shader content error.",
                "Cannot load shader content. Reason:\n".concat(e.getMessage()),
                new String[]{
                        "Error in content loader",
                        "Inability to access content",
                        "Another error"
                },
                new String[]{
                        "Make sure your content loader is working properly (if you are using a custom loader)",
                        "Make sure you store or have access to the shader content",
                        "Check out the details"
                }
        );
    }
}
