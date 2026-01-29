package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.NonNull;

/**
 * Объект, представляющий собой контент glsl файла, используемые в различных целях.
 * Данные объекты позволяют хранить контент в памяти и использовать его повторно бесконечное количество раз, повторно не загружая его.
 * Данная реализация хорошо может хорошо оптимизировать моменты, когда вам надо, например, для большого количество программ использовать один и тот же шейдер.
 */
public class GlslFileEntry {
    /** Реестр, хранящий всю нужную и дополнительную информацию о контенте. **/
    @Getter
    private final Registry registry;

    public GlslFileEntry(@NonNull String name, GlslContent content, @NonNull String type, @NonNull Registry registry) {
        this.registry = registry;

        registry.computeIfAbsent(CometTags.NAME, name, true);
        registry.computeIfAbsent(CometTags.CONTENT, content, false);
        registry.computeIfAbsent(CometTags.TYPE, type, true);
    }

    public GlslFileEntry(GlslFileEntry instance) {
        this.registry = new Registry(instance.getRegistry());

        registry.set(CometTags.CONTENT, new GlslContent(instance.getContent()));
    }

    public String getName() {
        return this.registry.get(CometTags.NAME).orElseThrow().getValue();
    }

    public GlslContent getContent() {
        return this.registry.get(CometTags.CONTENT).orElseThrow().getValue();
    }

    public String getType() {
        return this.registry.get(CometTags.TYPE).orElseThrow().getValue();
    }
}
