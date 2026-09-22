package com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable;

import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
public interface UBOBindable {

    void bind(int index);
}
