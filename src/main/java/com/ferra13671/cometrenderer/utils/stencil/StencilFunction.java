package com.ferra13671.cometrenderer.utils.stencil;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
public record StencilFunction(@NonNull AlphaFunction function, int ref, int mask) {
}
