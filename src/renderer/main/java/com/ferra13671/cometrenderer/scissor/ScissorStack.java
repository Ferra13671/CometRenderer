package com.ferra13671.cometrenderer.scissor;

import java.util.Objects;
import java.util.Stack;

public class ScissorStack extends Stack<ScissorRect> {

    @Override
    public ScissorRect push(ScissorRect rect) {
        ScissorRect scissorRect = rect;
        if (!isEmpty())
            scissorRect = Objects.requireNonNullElse(rect.intersection(peek()), new ScissorRect(0, 0, 0, 0));

        return super.push(scissorRect);
    }
}
