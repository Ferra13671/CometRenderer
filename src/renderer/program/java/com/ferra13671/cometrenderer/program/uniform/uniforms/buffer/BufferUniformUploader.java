package com.ferra13671.cometrenderer.program.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

/**
 * Установщик буффера, принимающий объект буффера и устанавливающий её в униформу.
 *
 * @param uploadConsumer метод установки буффера.
 * @param <T> буффер.
 */
public record BufferUniformUploader<T>(BiConsumer<BufferUniform, T> uploadConsumer) {

    public static final BufferUniformUploader<GpuBuffer> GPU_BUFFER = new BufferUniformUploader<>(
            (bufferUniform, gpuBuffer) -> {
                GL32.glBindBufferBase(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBufferIndex(),
                        gpuBuffer.getId()
                );
            }
    );
}
