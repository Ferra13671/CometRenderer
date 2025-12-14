package com.ferra13671.cometrenderer.tag;

public interface TagEntry<T> {

    Tag<T> getTag();

    T getValue();

    void setValue(T value);
}
