package com.ferra13671.cometrenderer.program.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL31;

/*
 * Униформа, хранящая в себе буффер, который в последствии разбивается на другие юниформы
 */
public class BufferUniform extends GlUniform {
    private final int blockBinding;
    private Runnable uploadRunnable = null;

    public BufferUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        int index = GL31.glGetUniformBlockIndex(glProgram.getId(), name);
        if (index == -1) {
            ExceptionPrinter.printAndExit(new NoSuchUniformException(name, glProgram.getName()));
            blockBinding = -1;
        } else {
            blockBinding = glProgram.getBuffersBindingsAmount() + 1;
            glProgram.setBuffersBindingsAmount(blockBinding);
            GL31.glUniformBlockBinding(glProgram.getId(), index, blockBinding);
        }
    }

    public int getBlockBinding() {
        return blockBinding;
    }

    public void set(GpuBufferSlice gpuBufferSlice) {
        this.uploadRunnable = () -> BufferUniformApplier.GPU_BUFFER_SLICE.uploadConsumer().accept(this, gpuBufferSlice);
    }

    public void set(GlGpuBuffer glGpuBuffer) {
        this.uploadRunnable = () -> BufferUniformApplier.GL_GPU_BUFFER.uploadConsumer().accept(this, glGpuBuffer);
    }

    public void set(GpuBuffer gpuBuffer) {
        this.uploadRunnable = () -> BufferUniformApplier.GPU_BUFFER.uploadConsumer().accept(this, gpuBuffer);
    }

    public <T> void set(BufferUniformApplier<T> uploader, T buffer) {
        this.uploadRunnable = () -> uploader.uploadConsumer().accept(this, buffer);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        if (this.uploadRunnable != null)
            this.uploadRunnable.run();
    }
}
