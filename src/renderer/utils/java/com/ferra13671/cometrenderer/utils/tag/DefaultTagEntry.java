package com.ferra13671.cometrenderer.utils.tag;

public class DefaultTagEntry<T> implements TagEntry<T> {
    private final Tag<T> tag;
    private T value;

    public DefaultTagEntry(Tag<T> tag, T value) {
        this.tag = tag;
        this.value = value;
    }

    @Override
    public Tag<T> getTag() {
        return this.tag;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
