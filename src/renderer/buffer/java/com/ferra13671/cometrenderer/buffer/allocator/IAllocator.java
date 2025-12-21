package com.ferra13671.cometrenderer.buffer.allocator;

import java.nio.ByteBuffer;

public interface IAllocator {

    long allocate(int size);

    boolean isEmpty();

    ByteBuffer getBuffer();

    void close();
}
