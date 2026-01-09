package com.ferra13671.cometrenderer.plugins.minecraft.drawer;

import java.io.Closeable;

public interface IDrawer extends Closeable {

    IDrawer tryDraw();

    boolean isBuilt();

    IDrawer build();

    IDrawer makeStandalone();

    @Override
    void close();
}
