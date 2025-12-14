package com.ferra13671.cometrenderer.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.tag.Registry;

/**
 * Объект, представляющий собой контент glsl файла, используемые в различных целях.
 * Данные объекты позволяют хранить контент в памяти и использовать его повторно бесконечное количество раз, повторно не загружая его.
 * Данная реализация хорошо может хорошо оптимизировать моменты, когда вам надо, например, для большого количество программ использовать один и тот же шейдер.
 */
public class GlslFileEntry {
    /** Реестр, хранящий всю нужную и дополнительную информацию о контенте. **/
    private final Registry registry;

    public GlslFileEntry(String name, String content, String type, Registry registry) {
        this.registry = registry;

        registry.computeIfAbsent(CometTags.NAME, name, true);
        registry.computeIfAbsent(CometTags.CONTENT, content, false);
        registry.computeIfAbsent(CometTags.TYPE, type, true);
    }

    public String getName() {
        return this.registry.get(CometTags.NAME).orElseThrow().getValue();
    }

    public String getContent() {
        return this.registry.get(CometTags.CONTENT).orElseThrow().getValue();
    }

    public String getType() {
        return this.registry.get(CometTags.TYPE).orElseThrow().getValue();
    }

    public Registry getRegistry() {
        return this.registry;
    }
}
