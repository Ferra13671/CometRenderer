package com.ferra13671.cometrenderer.shadercolor;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
@FunctionalInterface
public interface ShaderColorScope extends AutoCloseable {

    @Override
    void close();
}