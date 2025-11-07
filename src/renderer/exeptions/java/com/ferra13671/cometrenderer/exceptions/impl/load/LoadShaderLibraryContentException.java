package com.ferra13671.cometrenderer.exceptions.impl.load;

import com.ferra13671.cometrenderer.exceptions.CometException;

/**
 * Ошибка, вызываемая в том случае, когда при загрузке контента шейдерной библиотеки произошла ошибка.
 */
public class LoadShaderLibraryContentException extends CometException {
    @java.io.Serial
    private static final long serialVersionUID = -6809337209489732866L;

    public LoadShaderLibraryContentException(Exception e) {
        super(
                "Load library content error.",
                "Cannot load library content. Reason:\n".concat(e.getMessage()),
                new String[]{
                        "Error in content loader",
                        "Inability to access content",
                        "Another error"
                },
                new String[]{
                        "Make sure your content loader is working properly (if you are using a custom loader)",
                        "Make sure you store or have access to the library content",
                        "Check out the details"
                }
        );
    }
}
