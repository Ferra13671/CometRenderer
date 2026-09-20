package com.ferra13671.cometrenderer.utils.stencil;

import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
public record StencilOp(@NonNull StencilOpAction stencilFailed, @NonNull StencilOpAction stencilPassedDepthFailed, @NonNull StencilOpAction allPassed) {
}
