package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.9")
public interface IndexList {

    void index(int index);

    void indexes(int... indexes);
}
