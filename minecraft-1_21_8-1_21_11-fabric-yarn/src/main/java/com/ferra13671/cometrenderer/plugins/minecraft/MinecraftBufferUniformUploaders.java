package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.program.uniform.uniforms.buffer.BufferUniformUploader;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL32;

public class MinecraftBufferUniformUploaders {

    public static final BufferUniformUploader<GpuBufferSlice> GPU_BUFFER_SLICE = new BufferUniformUploader<>(
            (bufferUniform, gpuBufferSlice) ->
                    GL32.glBindBufferRange(
                            BufferTarget.UNIFORM_BUFFER.glId,
                            bufferUniform.getBufferIndex(),
                            MinecraftPlugin.getBufferIdGetter().apply((GlGpuBuffer) gpuBufferSlice.buffer()),
                            gpuBufferSlice.offset(),
                            gpuBufferSlice.length()
                    )
    );
    public static final BufferUniformUploader<GlGpuBuffer> GL_GPU_BUFFER = new BufferUniformUploader<>(
            (bufferUniform, glGpuBuffer) ->
                    GL32.glBindBufferBase(
                            BufferTarget.UNIFORM_BUFFER.glId,
                            bufferUniform.getBufferIndex(),
                            MinecraftPlugin.getBufferIdGetter().apply(glGpuBuffer)
                    )
    );
}
