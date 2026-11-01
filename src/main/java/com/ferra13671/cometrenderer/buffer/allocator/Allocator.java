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

    protected boolean closed;

    public Allocator(int size) {
        this.pointer = ALLOCATOR.malloc(size);
        this.size = size;
        this.cachedBuffer = MemoryUtil.memByteBuffer(this.pointer, this.size);
    }

    public long allocate(int size) {
        if (this.closed)
            ErrorHandlers.onAllocatorClosed();

        if (this.offset + size > this.size)
            ErrorHandlers.onAllocatorOverflow(size, this.size, this.size - this.offset);

        long address = this.offset;
        this.offset += size;
        return this.pointer + address;
    }

    @Override
    public boolean isEmpty() {
        if (this.closed)
            ErrorHandlers.onAllocatorClosed();

        return this.offset == 0L;
    }

    @Override
    public void clear() {
        if (this.closed)
            ErrorHandlers.onAllocatorClosed();

        this.offset = 0L;
    }

    @Override
    public ByteBuffer getBuffer() {
        if (this.closed)
            ErrorHandlers.onAllocatorClosed();

        this.cachedBuffer.limit((int) this.offset);
        return this.cachedBuffer;
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;

            ALLOCATOR.free(this.pointer);
            this.pointer = 0L;
        }
    }
}
