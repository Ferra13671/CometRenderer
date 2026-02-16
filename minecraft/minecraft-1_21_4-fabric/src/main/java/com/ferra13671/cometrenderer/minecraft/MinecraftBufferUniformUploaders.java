package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.BufferUniform;
import com.mojang.blaze3d.buffers.GpuBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

public class MinecraftBufferUniformUploaders {

    public static final BiConsumer<BufferUniform, GpuBuffer> GPU_BUFFER = (bufferUniform, gpuBuffer) ->
            GL32.glBindBufferBase(
                    BufferTarget.UNIFORM_BUFFER.glId,
                    bufferUniform.getBufferIndex(),
                    gpuBuffer.handle
            );
}
