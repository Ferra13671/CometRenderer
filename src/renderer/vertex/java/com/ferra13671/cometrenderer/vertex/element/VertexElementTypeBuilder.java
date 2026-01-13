package com.ferra13671.cometrenderer.vertex.element;

import com.ferra13671.cometrenderer.utils.Builder;

import java.util.function.BiConsumer;

public class VertexElementTypeBuilder<T> extends Builder<VertexElementType<T>>  {
    private String name;
    private Integer size;
    private Integer offset;
    private Integer glId;
    private Class<T> clazz;
    private BiConsumer<Long, T[]> uploadConsumer;

    public VertexElementTypeBuilder() {
        super("vertex element type");
    }

    public VertexElementTypeBuilder<T> name(String name) {
        this.name = name;

        return this;
    }

    public VertexElementTypeBuilder<T> size(Integer size) {
        this.size = size;

        return this;
    }

    public VertexElementTypeBuilder<T> offset(Integer offset) {
        this.offset = offset;

        return this;
    }

    public VertexElementTypeBuilder<T> glId(Integer glId) {
        this.glId = glId;

        return this;
    }

    public VertexElementTypeBuilder<T> clazz(Class<T> clazz) {
        this.clazz = clazz;

        return this;
    }

    public VertexElementTypeBuilder<T> uploadConsumer(BiConsumer<Long, T[]> uploadConsumer) {
        this.uploadConsumer = uploadConsumer;

        return this;
    }

    @Override
    public VertexElementType<T> build() {
        assertNotNull(this.name, "name");
        if (this.offset == null)
            this.offset = this.size;
        if (this.size == null)
            this.size = this.offset;
        assertNotNull(this.size, "size");
        assertNotNull(this.glId, "glId");
        assertNotNull(this.clazz, "type class");
        assertNotNull(this.uploadConsumer, "upload consumer");

        return new VertexElementType<>(
                this.name,
                this.size,
                this.offset,
                this.glId,
                this.clazz,
                this.uploadConsumer
        );
    }
}
