package com.ferra13671.cometrenderer.stencil;

import com.ferra13671.cometrenderer.utils.CompareFunction;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
public record StencilFunction(@NonNull CompareFunction function, int ref, int mask) {
}
