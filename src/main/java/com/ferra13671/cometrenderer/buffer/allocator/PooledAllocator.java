package com.ferra13671.cometrenderer.buffer.allocator;

import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.1")
class PooledAllocator extends Allocator {
    private final AllocatorPool pool;
    private boolean destroyed = false;

    PooledAllocator(int size, AllocatorPool pool) {
        super(size);

        this.pool = pool;
    }

    void acquire() {
        this.closed = false;
        clear();
    }

    void destroy() {
        if (!this.destroyed) {
            this.destroyed = true;
            this.closed = false;
            super.close();
        }
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;

            this.pool.release(this);
        }
    }
}
