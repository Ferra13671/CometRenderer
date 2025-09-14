package com.ferra13671.cometrenderer.vertex;

import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.BufferAllocator;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/*
 * Модифицированная версия BuiltBuffer, которая при создании создаёт свой локальный буффер
 *  вершин, тем самым не завися от аллокатора, что позволяет закрывать аллокатор после компиляции вершин
 */
public class BuiltVertexBuffer extends BuiltBuffer {
    private final ByteBuffer vertexBuffer;

    public BuiltVertexBuffer(ByteBuffer buffer, DrawParameters drawParameters) {
        super(null, drawParameters);
        //Создаём локальный буффер с тем же размером, что и буффер аллокатора
        vertexBuffer = MemoryUtil.memAlloc(buffer.capacity());
        //Копируем данный буффера аллокатора в наш буффер
        MemoryUtil.memCopy(buffer, vertexBuffer);
    }

    /*
     * В данном BuiltBuffer не поддерживается сортировка вершин
     */
    @Override
    public SortState sortQuads(BufferAllocator allocator, VertexSorter sorter) {
        throw new IllegalStateException("Cannot invoke 'sortQuads' in BuiltVertexBuffer");
    }

    @Override
    public ByteBuffer getBuffer() {
        //Возвращаем локальный буффер вершин
        return vertexBuffer;
    }

    @Override
    public void close() {
        //Вместо очистки аллокатора очищаем наш локальный буффер вершин
        MemoryUtil.memFree(vertexBuffer);
    }
}
