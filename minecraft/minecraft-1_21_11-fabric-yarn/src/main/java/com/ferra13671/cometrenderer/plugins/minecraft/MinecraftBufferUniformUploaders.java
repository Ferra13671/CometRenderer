package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

public class MinecraftBufferUniformUploaders {

    public static final BiConsumer<BufferUniform, GpuBufferSlice> GPU_BUFFER_SLICE = (bufferUniform, gpuBufferSlice) ->
            GL32.glBindBufferRange(
                    BufferTarget.UNIFORM_BUFFER.glId,
                    bufferUniform.getBufferIndex(),
                    MinecraftPlugin.getBufferIdGetter().apply((GlGpuBuffer) gpuBufferSlice.buffer()),
                    gpuBufferSlice.offset(),
                    gpuBufferSlice.length()
            );
    public static final BiConsumer<BufferUniform, GlGpuBuffer> GL_GPU_BUFFER = (bufferUniform, glGpuBuffer) ->
            GL32.glBindBufferBase(
                    BufferTarget.UNIFORM_BUFFER.glId,
                    bufferUniform.getBufferIndex(),
                    MinecraftPlugin.getBufferIdGetter().apply(glGpuBuffer)
            );
}
