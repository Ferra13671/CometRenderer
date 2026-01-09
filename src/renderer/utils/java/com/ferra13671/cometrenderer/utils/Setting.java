package com.ferra13671.cometrenderer.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Setting<T> {
    @Setter
    private T value;
    private final T defaultValue;

    public Setting(T value) {
        this.defaultValue = this.value = value;
    }

    public void toDefault() {
        this.value = this.defaultValue;
    }
}
