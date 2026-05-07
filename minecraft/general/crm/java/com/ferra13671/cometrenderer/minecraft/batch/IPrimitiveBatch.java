package com.ferra13671.cometrenderer.minecraft.batch;

import org.apiguardian.api.API;

import java.io.Closeable;

@API(status = API.Status.MAINTAINED, since = "2.2")
public interface IPrimitiveBatch extends Closeable {

    IPrimitiveBatch tryDraw();

    boolean isBuilt();

    IPrimitiveBatch build();

    IPrimitiveBatch makeStandalone();

    @Override
    void close();
}
