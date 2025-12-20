package com.ferra13671.cometrenderer;

public interface BufferRenderer<T> {

    void draw(T buffer, boolean close);
}
