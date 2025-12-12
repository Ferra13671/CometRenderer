package com.ferra13671.cometrenderer.compiler.tag;

import com.ferra13671.ferraguard.annotations.OverriddenMethod;

public class ImmutableTag<T> extends DefaultTag<T> {

    public ImmutableTag(Tag<T> tag, T value) {
        super(tag, value);
    }

    @Override
    @OverriddenMethod
    public void setValue(T value) {
        throw new UnsupportedOperationException("Unable to change value for ImmutableTagEntry");
    }
}
