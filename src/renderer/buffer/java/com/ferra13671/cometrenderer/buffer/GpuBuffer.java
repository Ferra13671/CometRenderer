package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.utils.Bindable;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import lombok.Getter;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

/**
 * Буффер, хранящийся на GPU и используемый в различных направлениях.
 */
public class GpuBuffer implements Bindable, AutoCloseable {
    /** Айди буффера в OpenGL. **/
    @Getter
    private final int id;
    /** Тип использования буффера. **/
    @Getter
    private final BufferUsage usage;
    /** Тип цели использования буффера. **/
    @Getter
    private final BufferTarget target;
    /** Закрыт буффер или нет. **/
    private boolean closed = false;

    /**
     * @param data информация, которая будет добавлена в GPU буффер.
     * @param usage тип использования буффера.
     * @param target тип цели использования буффера.
     */
    public GpuBuffer(ByteBuffer data, BufferUsage usage, BufferTarget target) {
        this(usage, target);

        bind();
        GL15.glBufferData(target.glId, data, usage.glId);
        unbind();
    }

    /**
     * @param capacity размер буффера.
     * @param usage тип использования буффера.
     * @param target тип цели использования буффера.
     */
    public GpuBuffer(int capacity, BufferUsage usage, BufferTarget target) {
        this(usage, target);

        bind();
        GL15.glBufferData(target.glId, capacity, usage.glId);
        unbind();
    }

    private GpuBuffer(BufferUsage usage, BufferTarget target) {
        this.id = GL15.glGenBuffers();
        this.usage = usage;
        this.target = target;
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
