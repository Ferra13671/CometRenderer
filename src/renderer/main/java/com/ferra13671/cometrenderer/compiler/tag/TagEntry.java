package com.ferra13671.cometrenderer.compiler.tag;

public interface TagEntry<T> {

    Tag<T> getTag();

    T getValue();

    void setValue(T value);
}
