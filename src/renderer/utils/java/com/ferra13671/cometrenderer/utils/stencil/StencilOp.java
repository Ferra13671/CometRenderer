package com.ferra13671.cometrenderer.utils.stencil;

import com.ferra13671.cometrenderer.utils.StencilOpAction;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "2.9")
public record StencilOp(@NonNull StencilOpAction stencilFailed, @NonNull StencilOpAction stencilPassedDepthFailed, @NonNull StencilOpAction allPassed) {
}
