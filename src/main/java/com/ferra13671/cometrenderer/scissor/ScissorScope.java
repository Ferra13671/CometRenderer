package com.ferra13671.cometrenderer.scissor;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
@FunctionalInterface
public interface ScissorScope extends AutoCloseable {

    @Override
    void close();
}