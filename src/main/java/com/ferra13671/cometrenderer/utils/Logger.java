package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0")
public interface Logger {

    void log(String message);

    void warn(String message);

    void error(String message);
}
