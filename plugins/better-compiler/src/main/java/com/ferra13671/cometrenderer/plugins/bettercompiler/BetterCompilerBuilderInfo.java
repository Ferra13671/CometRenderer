package com.ferra13671.cometrenderer.plugins.bettercompiler;

import lombok.NonNull;
import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@API(status = API.Status.MAINTAINED, since = "2.5")
public class BetterCompilerBuilderInfo {
    @API(status = API.Status.MAINTAINED, since = "3.1")
    private final HashMap<String, String> definitions = new HashMap<>();
    private final HashMap<String, String> definedConstants = new HashMap<>();
    private final HashMap<String, String> definedMethods = new HashMap<>();

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public BetterCompilerBuilderInfo define(@NonNull String name, @NonNull String value) {
        this.definitions.put(name, value);

        return this;
    }

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public BetterCompilerBuilderInfo define(@NonNull String name) {
        return define(name, "");
    }

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public Map<String, String> getDefinitions() {
        return Collections.unmodifiableMap(this.definitions);
    }

    public BetterCompilerBuilderInfo defineConstant(@NonNull String name, @NonNull String value) {
        this.definedConstants.put(name, value);

        return this;
    }

    public Optional<String> getConstant(@NonNull String name) {
        return Optional.ofNullable(this.definedConstants.get(name));
    }

    public String getConstantOrDefault(@NonNull String name, @NonNull String defaultValue) {
        return getConstant(name).orElse(defaultValue);
    }

    public BetterCompilerBuilderInfo defineMethod(@NonNull String name, @NonNull String content) {
        this.definedMethods.put(name, content);

        return this;
    }

    public Optional<String> getMethodContent(@NonNull String name) {
        return Optional.ofNullable(this.definedMethods.get(name));
    }

    public String getMethodContentOrDefault(@NonNull String name, @NonNull String defaultContent) {
        return getConstant(name).orElse(defaultContent);
    }
}
