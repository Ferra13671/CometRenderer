package com.ferra13671.cometrenderer.buffer;

import org.lwjgl.opengl.GL15;

/**
 * Тип использования GPU буффера.
 *
 * @see GpuBuffer
 */
public enum BufferUsage {
    STREAM_DRAW(GL15.GL_STREAM_DRAW),
    STATIC_DRAW(GL15.GL_STATIC_DRAW),
    DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
    STREAM_READ(GL15.GL_STREAM_READ),
    STATIC_READ(GL15.GL_STATIC_READ),
    DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
    STREAM_COPY(GL15.GL_STREAM_COPY),
    STATIC_COPY(GL15.GL_STATIC_COPY),
    DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY);

    /** Айди типа использования в OpenGL. **/
    public final int glId;

    /**
     * @param glId айди типа использования в OpenGL.
     */
    BufferUsage(int glId) {
        this.glId = glId;
    }
}
