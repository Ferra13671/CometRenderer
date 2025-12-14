package com.ferra13671.cometrenderer.tag;

public class DefaultTag<T> implements TagEntry<T> {
    private final Tag<T> tag;
    private T value;

    public DefaultTag(Tag<T> tag, T value) {
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
