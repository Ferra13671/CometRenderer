package com.ferra13671.cometrenderer.utils.setting;

public class NumberSetting<T extends Number> extends Setting<T> {
    private final T min;
    private final T max;

    public NumberSetting(T value, T min, T max) {
        super(value);
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(T value) {
        if (this.min.doubleValue() > value.doubleValue())
            value = this.min;

        if (this.max.doubleValue() < value.doubleValue())
            value = this.max;

        super.setValue(value);
    }
}
