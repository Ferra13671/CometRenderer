package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.minecraft.mixins.IGlBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

public class MinecraftBufferUniformUploaders {

    public static final BiConsumer<BufferUniform, GpuBufferSlice> GPU_BUFFER_SLICE = (bufferUniform, gpuBufferSlice) ->
            GL32.glBindBufferRange(
                    BufferTarget.UNIFORM_BUFFER.glId,
                    bufferUniform.getBufferIndex(),
                    ((IGlBuffer) gpuBufferSlice.buffer())._getHandle(),
                    gpuBufferSlice.offset(),
                    gpuBufferSlice.length()
            );
    public static final BiConsumer<BufferUniform, GlBuffer> GL_GPU_BUFFER = (bufferUniform, glGpuBuffer) ->
            GL32.glBindBufferBase(
                    BufferTarget.UNIFORM_BUFFER.glId,
                    bufferUniform.getBufferIndex(),
                    ((IGlBuffer) glGpuBuffer)._getHandle()
            );
}
