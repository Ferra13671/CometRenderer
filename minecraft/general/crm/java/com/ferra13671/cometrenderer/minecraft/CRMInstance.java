package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;import com.ferra13671.cometrenderer.scissor.ScissorRect;

import java.util.function.Supplier;

public class CRMInstance {
    private final Supplier<Integer> scaleGetter;

    public CRMInstance(Supplier<Integer> scaleGetter) {
        this.scaleGetter = scaleGetter;
    }

    public void setupUIProjection() {
        CRM.controller.setupUIProjection(this.scaleGetter.get());
    }

    public void restoreUIProjection() {
        CRM.controller.restoreUIProjection();
    }

    public void pushScissor(ScissorRect scissorRect) {
        CometRenderer.getScissorStack().push(fixScissorRect(scissorRect));
    }

    private ScissorRect fixScissorRect(ScissorRect scissorRect) {
        int scale = this.scaleGetter.get();

        return new ScissorRect(
                scissorRect.x() * scale,
                CRM.getMainFramebufferHeight() - ((scissorRect.y() + scissorRect.height()) * scale),
                scissorRect.width() * scale,
                scissorRect.height() * scale
        );
    }
}
