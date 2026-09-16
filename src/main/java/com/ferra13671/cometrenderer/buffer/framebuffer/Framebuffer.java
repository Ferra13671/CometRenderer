package com.ferra13671.cometrenderer.buffer.framebuffer;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0")
public interface Framebuffer {

    void resize(int width, int height);

    int getWidth();

    int getHeight();

    int getId();

    void bind(boolean setViewport);

    @API(status = API.Status.MAINTAINED, since = "3.0")
    void bindRead();

    @API(status = API.Status.MAINTAINED, since = "3.0")
    void bindDraw(boolean setViewport);

    int getColorTextureId();

    int getDepthAndStencilTextureId();

    void clearColor();

    void clearDepth();

    void clearStencil();

    void clearAll();

    void delete();
}
