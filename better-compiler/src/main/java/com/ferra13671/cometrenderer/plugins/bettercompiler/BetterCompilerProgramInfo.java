package com.ferra13671.cometrenderer.plugins.bettercompiler;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

public class BetterCompilerProgramInfo {
    private final HashMap<String, String> definedConstants = new HashMap<>();
    private final HashMap<String, String> definedMethods = new HashMap<>();

    public void defineConstant(@NonNull String name, @NonNull String value) {
        this.definedConstants.put(name, value);
    }

    public Optional<String> getConstant(@NonNull String name) {
        return Optional.ofNullable(this.definedConstants.get(name));
    }

    public String getConstantOrDefault(@NonNull String name, @NonNull String defaultValue) {
        return getConstant(name).orElse(defaultValue);
    }

    public void defineMethod(@NonNull String name, @NonNull String content) {
        this.definedMethods.put(name, content);
    }

    public Optional<String> getMethodContent(@NonNull String name) {
        return Optional.ofNullable(this.definedMethods.get(name));
    }

    public String getMethodContentOrDefault(@NonNull String name, @NonNull String defaultContent) {
        return getConstant(name).orElse(defaultContent);
    }
}
