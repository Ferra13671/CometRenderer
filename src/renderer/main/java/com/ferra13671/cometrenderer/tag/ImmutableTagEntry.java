package com.ferra13671.cometrenderer.tag;

import com.ferra13671.ferraguard.annotations.OverriddenMethod;

public class ImmutableTagEntry<T> extends DefaultTagEntry<T> {

    public ImmutableTagEntry(Tag<T> tag, T value) {
        super(tag, value);
    }

    @Override
    @OverriddenMethod
    public void setValue(T value) {
        throw new UnsupportedOperationException("Unable to change value for ImmutableTagEntry");
    }
}
