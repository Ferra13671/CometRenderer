package com.ferra13671.cometrenderer.utils.setting;

import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

@Getter
@API(status = API.Status.MAINTAINED)
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
