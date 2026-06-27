package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED)
public interface BufferRenderer<T> {

    void draw(T buffer, boolean close);
}
