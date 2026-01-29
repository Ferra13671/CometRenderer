package com.ferra13671.cometrenderer.utils.tag;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class Registry {
    private final HashMap<Tag<?>, TagEntry<?>> tagMap = new HashMap<>();

    public Registry() {}

    public Registry(Registry instance) {
        instance.forEach(this::set);
    }

    public <T> void set(Tag<T> tag, T value) {
        set(new DefaultTagEntry<>(tag, value));
    }

    public <T> void setImmutable(Tag<T> tag, T value) {
        set(new ImmutableTagEntry<>(tag, value));
    }

    public <T> void set(TagEntry<T> entry) {
        if (contains(entry.getTag()) && get(entry.getTag()).orElseThrow() instanceof ImmutableTagEntry<?>)
            throw new UnsupportedOperationException("Unable to change value for ImmutableTagEntry");

        this.tagMap.put(entry.getTag(), entry);
    }

    public boolean contains(Tag<?> tag) {
        return this.tagMap.containsKey(tag);
    }

    public <T> Optional<TagEntry<T>> get(Tag<T> tag) {
        return Optional.ofNullable((TagEntry<T>) this.tagMap.get(tag));
    }

    public <T> TagEntry<T> computeIfAbsent(Tag<T> tag, T value, boolean immutable) {
        TagEntry<T> tagEntry = (TagEntry<T>) this.tagMap.get(tag);
        if (tagEntry == null) {
            tagEntry = immutable ? new ImmutableTagEntry<>(tag, value) : new DefaultTagEntry<>(tag, value);
            set(tagEntry);
        }

        return tagEntry;
    }

    public void forEach(Consumer<TagEntry<?>> consumer) {
        this.tagMap.values().forEach(consumer);
    }

    public void forEachTags(Consumer<Tag<?>> consumer) {
        this.tagMap.keySet().forEach(consumer);
    }
}
