package com.ferra13671.cometrenderer.vertex;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.BufferAllocator;

/*
 * BufferBuilder, имеющий свой аллокатор, позволяющий создавать одновременно несколько билдеров
 */
public class VertexBuilder extends BufferBuilder {
    private final BufferAllocator bufferAllocator;

    private VertexBuilder(BufferAllocator allocator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
        super(allocator, drawMode, vertexFormat);
        this.bufferAllocator = allocator;
    }

    /*
     * Билдит вертекс билдер и закрывает его аллокатор
     */
    @Override
    public BuiltBuffer endNullable() {
        BuiltBuffer builtBuffer = super.endNullable();
        builtBuffer = builtBuffer == null ? null : new BuiltVertexBuffer(builtBuffer.getBuffer(), builtBuffer.getDrawParameters());
        close();
        return builtBuffer;
    }

    /*
     * Закрывает аллокатор буффера. Нужно использовать после завершения работы с билдером, иначе могут быть утечки памяти
     */
    public void close() {
        bufferAllocator.close();
    }

    /*
     * Создаёт вертекс билдер с дефолтным размером аллокатора
     */
    public static VertexBuilder create(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
        return create(786432, drawMode, vertexFormat);
    }

    /*
     * Создаёт вертекс билдер с данным размером аллокатора
     */
    public static VertexBuilder create(int size, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
        return new VertexBuilder(new BufferAllocator(size), drawMode, vertexFormat);
    }
}
