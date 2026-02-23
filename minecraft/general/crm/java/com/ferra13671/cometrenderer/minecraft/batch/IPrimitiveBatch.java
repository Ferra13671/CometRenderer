package com.ferra13671.cometrenderer.minecraft.batch;

import java.io.Closeable;

public interface IPrimitiveBatch extends Closeable {

    IPrimitiveBatch tryDraw();

    boolean isBuilt();

    IPrimitiveBatch build();

    IPrimitiveBatch makeStandalone();

    @Override
    void close();
}
