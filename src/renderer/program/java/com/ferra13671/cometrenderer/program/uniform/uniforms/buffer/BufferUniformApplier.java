package com.ferra13671.cometrenderer.program.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

public record BufferUniformApplier<T>(BiConsumer<BufferUniform, T> uploadConsumer) {

    public static final BufferUniformApplier<GpuBufferSlice> GPU_BUFFER_SLICE = new BufferUniformApplier<>(
            (bufferUniform, gpuBufferSlice) ->
                GL32.glBindBufferRange(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBlockBinding(),
                        CometRenderer.getBufferIdGetter().apply((GlGpuBuffer) gpuBufferSlice.buffer()),
                        gpuBufferSlice.offset(),
                        gpuBufferSlice.length()
                )
    );
    public static final BufferUniformApplier<GlGpuBuffer> GL_GPU_BUFFER = new BufferUniformApplier<>(
            (bufferUniform, glGpuBuffer) ->
                GL32.glBindBufferBase(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBlockBinding(),
                        CometRenderer.getBufferIdGetter().apply(glGpuBuffer)
                )
    );
    public static final BufferUniformApplier<GpuBuffer> GPU_BUFFER = new BufferUniformApplier<>(
            (bufferUniform, gpuBuffer) -> {
                GL32.glBindBufferBase(
                        BufferTarget.UNIFORM_BUFFER.glId,
                        bufferUniform.getBlockBinding(),
                        gpuBuffer.getId()
                );
            }
    );
}
