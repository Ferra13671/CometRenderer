package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

@API(status = API.Status.DEPRECATED, since = "3.1")
public interface BufferRenderer<T> {

    void draw(T buffer, boolean close);
}
