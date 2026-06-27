package com.ferra13671.cometrenderer.buffer.allocator;

import com.ferra13671.cometrenderer.ErrorHandlers;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class Allocator implements IAllocator {
    private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);
    private long pointer;
    private long offset;
    private final int size;
    private final ByteBuffer cachedBuffer;

    public Allocator(int size) {
        this.pointer = ALLOCATOR.malloc(size);
        this.size = size;
        this.cachedBuffer = MemoryUtil.memByteBuffer(this.pointer, this.size);
    }

    public long allocate(int size) {
        if (this.offset + size > this.size)
            ErrorHandlers.onAllocatorOverflow(size, this.size, this.size - this.offset);

        long address = this.offset;
        this.offset += size;
        return this.pointer + address;
    }

    @Override
    public boolean isEmpty() {
        return this.offset == 0L;
    }

    @Override
    public void clear() {
        this.offset = 0L;
    }

    @Override
    public ByteBuffer getBuffer() {
        this.cachedBuffer.limit((int) this.offset);
        return this.cachedBuffer;
    }

    @Override
    public void close() {
        ALLOCATOR.free(this.pointer);
        this.pointer = 0L;
    }
}
