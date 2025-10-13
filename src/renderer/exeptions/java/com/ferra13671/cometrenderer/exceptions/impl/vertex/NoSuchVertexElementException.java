package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class NoSuchVertexElementException extends CometException {

    @java.io.Serial
    private static final long serialVersionUID = 1835464041015596438L;

    public NoSuchVertexElementException(String elementName) {
        super(
                "No such vertex element.",
                String.format("Cannot find vertex element '%s'.", elementName),
                new String[]{
                        "You may have misspelled the vertex element name.",
                        "When creating a VertexBuilder, you selected the wrong VertexBuilder."
                },
                new String[]{
                        "Check that you have written the names of the vertex elements correctly in both the vertex format builder and the vertex builder method.",
                        "Check that you selected the correct vertex format when creating VertexBuilder."
                }
        );
    }
}
