package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.utils.Bindable;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

/**
 * Буффер, хранящийся на GPU и используемый в различных направлениях.
 */
public class GpuBuffer implements Bindable, AutoCloseable {
    /** Айди буффера в OpenGL. **/
    private final int id;
    /** Тип использования буффера. **/
    private final BufferUsage usage;
    /** Тип цели использования буффера. **/
    private final BufferTarget target;
    /** Закрыт буффер или нет. **/
    private boolean closed = false;

    /**
     * @param data информация, которая будет добавлена в GPU буффер.
     * @param usage тип использования буффера.
     * @param target тип цели использования буффера.
     */
    public GpuBuffer(ByteBuffer data, BufferUsage usage, BufferTarget target) {
        this.id = GL15.glGenBuffers();
        this.usage = usage;
        this.target = target;
        bind();
        GL15.glBufferData(target.glId, data, usage.glId);
        GL15.glBindBuffer(target.glId, 0);
    }

    /**
     * Возвращает айди буффера в OpenGL.
     *
     * @return айди буффера в OpenGL.
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает тип использования буффера.
     *
     * @return тип использования буффера.
     */
    public BufferUsage getUsage() {
        return usage;
    }

    /**
     * Возвращает тип цели использования буффера.
     *
     * @return тип цели использования буффера.
     */
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
