package com.ferra13671.cometrenderer.utils.tag;

import com.ferra13671.cometrenderer.utils.Pair;
import org.apiguardian.api.API;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@API(status = API.Status.MAINTAINED, since = "1.9")
public class Registry {
    private final Map<Tag<?>, Pair<Object, Boolean>> tagMap = new HashMap<>();

    public Registry() {}

    public Registry(Registry instance) {
        instance.forEach((tag, pair) -> this.tagMap.put(tag, (Pair<Object, Boolean>) pair));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public <T> void copyValue(Registry srcRegistry, Tag<T> tag) {
        if (srcRegistry.contains(tag))
            set(tag, srcRegistry.get(tag).orElse(null), srcRegistry.isImmutable(tag));
    }

    public <T> void set(Tag<T> tag, T value) {
        set(tag, value, false);
    }

    public <T> void setImmutable(Tag<T> tag, T value) {
        set(tag, value, true);
    }

    @API(status = API.Status.INTERNAL)
    public <T> void set(Tag<T> tag, T value, boolean immutable) {
        if (isImmutable(tag))
            throw new UnsupportedOperationException("Unable to change value for immutable tag.");

        this.tagMap.put(tag, new Pair<>(value, immutable));
    }

    public boolean contains(Tag<?> tag) {
        return this.tagMap.containsKey(tag);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public boolean isImmutable(Tag<?> tag) {
        return contains(tag) && this.tagMap.get(tag).right();
    }

    public <T> Optional<T> get(Tag<T> tag) {
        return Optional.ofNullable(!contains(tag) ? null : tag.cast(this.tagMap.get(tag).left()));
    }

    public <T> T computeIfAbsent(Tag<T> tag, T value, boolean immutable) {
        if (!contains(tag) && !isImmutable(tag))
            set(tag, value, immutable);

        return get(tag).orElse(null);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void forEach(BiConsumer<Tag<?>, Pair<?, Boolean>> consumer) {
        this.tagMap.forEach(consumer);
    }

    public void forEachTags(Consumer<Tag<?>> consumer) {
        this.tagMap.keySet().forEach(consumer);
    }
}
