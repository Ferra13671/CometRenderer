package com.ferra13671.cometrenderer.buffer.allocator;

import org.apiguardian.api.API;

import java.nio.ByteBuffer;

@API(status = API.Status.MAINTAINED, since = "2.1")
public interface IAllocator {

    long allocate(int size);

    boolean isEmpty();

    ByteBuffer getBuffer();

    void close();
}
