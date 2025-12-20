package com.ferra13671.cometrenderer.exceptions.impl;

import com.ferra13671.cometrenderer.exceptions.CometException;

public class AllocatorOverflowException extends CometException {

    public AllocatorOverflowException(long needSize, long maxAllocatorSize, long freeSize) {
        super(String.format("Unable to allocate '%s' bytes in allocator (allocator size: '%s'. free bytes: '%s').", needSize, maxAllocatorSize, freeSize));
    }
}
