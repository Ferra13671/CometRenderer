package com.ferra13671.cometrenderer.utils.tag;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.9")
public record Tag<T>(String id) {

    @API(status = API.Status.INTERNAL)
    public T map(Object value) {
        return (T) value;
    }
}
