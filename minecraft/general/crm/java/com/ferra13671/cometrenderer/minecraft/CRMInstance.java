package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;import com.ferra13671.cometrenderer.scissor.ScissorRect;
import org.apiguardian.api.API;

import java.util.function.Supplier;

@API(status = API.Status.STABLE, since = "2.6")
public class CRMInstance {
    private final Supplier<Integer> scaleGetter;

    public CRMInstance(Supplier<Integer> scaleGetter) {
        this.scaleGetter = scaleGetter;
    }

    public void setupUIMatrix() {
        CRM.controller.setupUIMatrix(this.scaleGetter.get());
    }

    public void restoreUIMatrix() {
        CRM.controller.restoreUIMatrix();
    }

    public void pushScissor(ScissorRect scissorRect) {
        CometRenderer.getScissorStack().push(CRM.fixScissorRect(scissorRect, this.scaleGetter.get()));
    }
}
