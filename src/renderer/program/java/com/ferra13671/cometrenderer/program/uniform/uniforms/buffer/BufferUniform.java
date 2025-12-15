package com.ferra13671.cometrenderer.program.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL31;

/**
 * Униформа, хранящая в себе параметр в виде буффера, который может быть разложен в программе на несколько данных.
 *
 * @see GlUniform
 * @see UniformType
 */
public class BufferUniform extends GlUniform {
    /** Индекс буффера униформы. **/
    private final int bufferIndex;
    /** Runnable, загружающий параметр в униформу. **/
    private Runnable uploadRunnable = null;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public BufferUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        int index = GL31.glGetUniformBlockIndex(glProgram.getId(), name);
        if (index == -1) {
            CometRenderer.manageException(new NoSuchUniformException(name, glProgram.getName()));
            bufferIndex = -1;
        } else {
            bufferIndex = glProgram.getBuffersIndexAmount() + 1;
            glProgram.setBuffersIndexAmount(bufferIndex);
            GL31.glUniformBlockBinding(glProgram.getId(), index, bufferIndex);
        }
    }

    /**
     * Возвращает индекс буффера униформы.
     *
     * @return индекс буффера униформы.
     */
    public int getBufferIndex() {
        return bufferIndex;
    }

    /**
     * Устанавливает буффер из GpuBufferSlice.
     *
     * @param gpuBufferSlice GpuBufferSlice.
     *
     * @see GpuBufferSlice
     */
    public void set(GpuBufferSlice gpuBufferSlice) {
        this.uploadRunnable = () -> BufferUniformUploader.GPU_BUFFER_SLICE.uploadConsumer().accept(this, gpuBufferSlice);
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает буффер из GlGpuBuffer.
     *
     * @param glGpuBuffer GlGpuBuffer.
     *
     * @see GlGpuBuffer
     */
    public void set(GlGpuBuffer glGpuBuffer) {
        this.uploadRunnable = () -> BufferUniformUploader.GL_GPU_BUFFER.uploadConsumer().accept(this, glGpuBuffer);
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает буффер из GpuBuffer.
     *
     * @param gpuBuffer GpuBuffer.
     *
     * @see GpuBuffer
     */
    public void set(GpuBuffer gpuBuffer) {
        this.uploadRunnable = () -> BufferUniformUploader.GPU_BUFFER.uploadConsumer().accept(this, gpuBuffer);
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает буффер при помощи пользовательского установщика.
     *
     * @param uploader установщик буффера.
     * @param buffer объект буффера.
     * @param <T> буффер.
     */
    public <T> void set(BufferUniformUploader<T> uploader, T buffer) {
        this.uploadRunnable = () -> uploader.uploadConsumer().accept(this, buffer);
        this.program.addUpdatedUniform(this);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        if (this.uploadRunnable != null)
            this.uploadRunnable.run();
    }
}
