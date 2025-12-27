package com.ferra13671.cometrenderer.exceptions.impl.vertex;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;

public class IllegalVertexElementStructureException extends CometException {

    public IllegalVertexElementStructureException(VertexElementType<?> type) {
        String reason = "";

        if (type.byteSize() <= 0)
            reason = String.format("byteSize(%s) must be > 0", type.byteSize());
        else if (type.offset() <= 0)
            reason = String.format("offset(%s) must be > 0", type.offset());
        else if (type.byteSize() % type.offset() != 0)
            reason = String.format("byteSize(%s) must be a multiple of offset(%s)", type.byteSize(), type.offset());
        else if (type.offset() > type.byteSize())
            reason = String.format("offset(%s) must be <= byteSize(%s)", type.offset(), type.byteSize());

        super(String.format("byteSize and offset in vertex element type '%s' do not match the condition:\n  %s.", type.typeName(), reason));
    }
}
