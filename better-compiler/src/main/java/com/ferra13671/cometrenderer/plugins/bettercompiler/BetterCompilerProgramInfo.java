package com.ferra13671.cometrenderer.plugins.bettercompiler;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

public class BetterCompilerProgramInfo {
    private final HashMap<String, String> constants = new HashMap<>();

    public void defineConstant(@NonNull String name, @NonNull String value) {
        this.constants.put(name, value);
    }

    public Optional<String> getConstant(@NonNull String name) {
        return Optional.ofNullable(this.constants.get(name));
    }

    public String getConstantOrDefault(@NonNull String name, @NonNull String defaultValue) {
        return getConstant(name).orElse(defaultValue);
    }
}
