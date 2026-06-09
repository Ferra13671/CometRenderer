package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import com.ferra13671.cometrenderer.utils.ColorMask;
import com.ferra13671.cometrenderer.utils.StencilOpAction;
import com.ferra13671.cometrenderer.utils.stencil.StencilFunction;
import com.ferra13671.cometrenderer.utils.stencil.StencilOp;
import lombok.Builder;
import org.apiguardian.api.API;

@Builder
@API(status = API.Status.EXPERIMENTAL, since = "2.9")
public record StencilInfo(Boolean stencilMask, Boolean depthMask, ColorMask colorMask, StencilFunction func, StencilOp op) {
    public static final StencilInfo DEFAULT_WRITE = StencilInfo.builder()
            .stencilMask(true)
            .depthMask(false)
            .colorMask(new ColorMask(false, false, false, false))
            .func(new StencilFunction(AlphaFunction.ALWAYS, 1, 255))
            .op(new StencilOp(StencilOpAction.KEEP, StencilOpAction.KEEP, StencilOpAction.REPLACE))
            .build();
    public static final StencilInfo DEFAULT_READ = StencilInfo.builder()
            .stencilMask(false)
            .depthMask(true)
            .colorMask(new ColorMask(true, true, true, true))
            .func(new StencilFunction(AlphaFunction.EQUAL, 0, 255))
            .op(new StencilOp(StencilOpAction.KEEP, StencilOpAction.KEEP, StencilOpAction.KEEP))
            .build();
}
