package com.ferra13671.cometrenderer.utils.tag;

public class ImmutableTagEntry<T> extends DefaultTagEntry<T> {

    public ImmutableTagEntry(Tag<T> tag, T value) {
        super(tag, value);
    }

    @Override
    public void setValue(T value) {
        throw new UnsupportedOperationException("Unable to change value for ImmutableTagEntry");
    }
}
