package com.ferra13671.cometrenderer.utils.tag;

public interface TagEntry<T> {

    Tag<T> getTag();

    T getValue();

    void setValue(T value);
}
