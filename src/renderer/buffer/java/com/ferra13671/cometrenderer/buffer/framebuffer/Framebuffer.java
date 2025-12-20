package com.ferra13671.cometrenderer.buffer.framebuffer;

public interface Framebuffer {

    void resize(int width, int height);

    int getWidth();

    int getHeight();

    void bind(boolean setViewport);

    int getColorTextureId();

    int getDepthTextureId();

    void clearColor();

    void clearDepth();

    void clearAll();

    void delete();
}
