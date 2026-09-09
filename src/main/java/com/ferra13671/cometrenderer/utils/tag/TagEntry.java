package com.ferra13671.cometrenderer.utils.tag;

import org.apiguardian.api.API;

//TODO remove
@API(status = API.Status.MAINTAINED)
public interface TagEntry<T> {

    Tag<T> getTag();

    T getValue();

    void setValue(T value);
}
