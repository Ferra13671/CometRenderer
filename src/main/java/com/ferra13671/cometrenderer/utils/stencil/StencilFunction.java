package com.ferra13671.cometrenderer.utils.stencil;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "2.9")
public record StencilFunction(@NonNull AlphaFunction function, int ref, int mask) {
}
