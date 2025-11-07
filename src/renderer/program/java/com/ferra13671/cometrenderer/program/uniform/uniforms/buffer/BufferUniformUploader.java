package com.ferra13671.cometrenderer.program.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

/**
 * Установщик буффера, принимающий объект буффера и устанавливающий её в униформу.
 *
 * @param uploadConsumer метод установки буффера.
 * @param <T> буффер.
 */
public record BufferUniformUploader<T>(BiConsumer<BufferUniform, T> uploadConsumer) {

    public static final BufferUniformUploader<GpuBufferSlice> GPU_BUFFER_SLICE = new BufferUniformUploader<>(
            (bufferUniform, gpuBufferSlice) ->
                GL32.glBindBufferRange(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBufferIndex(),
                        CometRenderer.getBufferIdGetter().apply((GlGpuBuffer) gpuBufferSlice.buffer()),
                        gpuBufferSlice.offset(),
                        gpuBufferSlice.length()
                )
    );
    public static final BufferUniformUploader<GlGpuBuffer> GL_GPU_BUFFER = new BufferUniformUploader<>(
            (bufferUniform, glGpuBuffer) ->
                GL32.glBindBufferBase(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBufferIndex(),
                        CometRenderer.getBufferIdGetter().apply(glGpuBuffer)
                )
    );
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
