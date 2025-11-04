package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.Bindable;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

public class GpuBuffer implements Bindable, AutoCloseable {
    private final int id;
    private final BufferUsage usage;
    private final BufferTarget target;
    private boolean closed = false;

    public GpuBuffer(ByteBuffer data, BufferUsage usage, BufferTarget target) {
        this.id = GL15.glGenBuffers();
        this.usage = usage;
        this.target = target;
        bind();
        GL15.glBufferData(target.glId, data, usage.glId);
        GL15.glBindBuffer(target.glId, 0);
    }

    public int getId() {
        return id;
    }

    public BufferUsage getUsage() {
        return usage;
    }

    public BufferTarget getTarget() {
        return target;
    }

    @Override
    @OverriddenMethod
    public void bind() {
        GL15.glBindBuffer(this.target.glId, this.id);
    }

    @Override
    @OverriddenMethod
    public void unbind() {
        GL15.glBindBuffer(this.target.glId, 0);
    }

    @Override
    @OverriddenMethod
    public void close() {
        if (!this.closed)
            GL15.glDeleteBuffers(this.id);
        this.closed = true;
    }
}
