package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.impl.AllocatorOverflowException;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class Allocator {
    private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);
    private int size;
    private int offset;
    private long pointer;
    private boolean closed = false;

    public Allocator(int size) {
        this.pointer = ALLOCATOR.malloc(size);
        this.size = size;
    }

    public long allocate(int size) {
        if (this.offset + size > this.size)
            CometRenderer.manageException(new AllocatorOverflowException(size, this.size, this.size - this.offset));

        long address = this.offset;
        this.offset += size;
        return Math.addExact(this.pointer, address);
    }

    public boolean isEmpty() {
        return this.pointer == 0;
    }

    public void clear() {
        this.offset = 0;
    }

    public ByteBuffer getBuffer() {
        return MemoryUtil.memByteBuffer(this.pointer, this.size);
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        ALLOCATOR.free(this.pointer);
        this.pointer = 0;
        this.size = 0;
        this.closed = true;
    }
}
