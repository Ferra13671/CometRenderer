package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;

public class IllegalVertexElementStructureException extends CometException {

    public IllegalVertexElementStructureException(VertexElementType<?> type, String reason) {
        super(String.format("size and offset in vertex element type '%s' do not match the condition:\n  %s.", type.name(), reason));
    }
}
