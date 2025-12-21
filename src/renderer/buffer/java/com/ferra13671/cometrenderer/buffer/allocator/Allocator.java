package com.ferra13671.cometrenderer.buffer.allocator;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.impl.AllocatorOverflowException;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class Allocator implements IAllocator {
    private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);
    private long pointer;
    private long offset;
    private final int size;

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

    @Override
    @OverriddenMethod
    public boolean isEmpty() {
        return this.offset == 0L;
    }

    @Override
    @OverriddenMethod
    public ByteBuffer getBuffer() {
        return MemoryUtil.memByteBuffer(this.pointer, (int) this.offset);
    }

    @Override
    @OverriddenMethod
    public void close() {
        ALLOCATOR.free(this.pointer);
        this.pointer = 0L;
    }
}
