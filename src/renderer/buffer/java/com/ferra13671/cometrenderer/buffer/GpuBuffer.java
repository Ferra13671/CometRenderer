package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.utils.Bindable;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

/**
 * Буффер, хранящийся на GPU и используемый в различных направлениях.
 */
@API(status = API.Status.MAINTAINED, since = "1.7.1")
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

        CometRenderer.getDevice().getDirectStateManager().bufferData(this, data);
    }

    /**
     * @param capacity размер буффера.
     * @param usage тип использования буффера.
     * @param target тип цели использования буффера.
     */
    public GpuBuffer(int capacity, BufferUsage usage, BufferTarget target) {
        this(usage, target);

        CometRenderer.getDevice().getDirectStateManager().bufferData(this, capacity);
    }

    private GpuBuffer(BufferUsage usage, BufferTarget target) {
        this.id = CometRenderer.getDevice().getDirectStateManager().createBuffer();
        this.usage = usage;
        this.target = target;
    }

    @Override
    public void bind() {
        GL15.glBindBuffer(this.target.glId, this.id);
    }

    @Override
    public void unbind() {
        GL15.glBindBuffer(this.target.glId, 0);
    }

    @Override
    public void close() {
        if (!this.closed)
            GL15.glDeleteBuffers(this.id);
        this.closed = true;
    }
}
