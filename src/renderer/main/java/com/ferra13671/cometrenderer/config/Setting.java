package com.ferra13671.cometrenderer.config;

public class Setting<T> {

    private T value;
    private final T defaultValue;

    public Setting(T value) {
        this.defaultValue = this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void toDefault() {
        this.value = this.defaultValue;
    }
}
