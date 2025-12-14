package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при загрузке glsl контента произошла ошибка.
 */
public class LoadGlslContentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -4206273236678422364L;

    public LoadGlslContentException(Exception e) {
        super(
                "Load glsl content error.",
                "Cannot load glsl content. Reason:\n".concat(e.getMessage()),
                new String[]{
                        "Error in content loader",
                        "Inability to access content",
                        "Another error"
                },
                new String[]{
                        "Make sure your content loader is working properly (if you are using a custom loader)",
                        "Make sure you store or have access to the glsl content",
                        "Check out the details"
                }
        );
    }
}
