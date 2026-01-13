package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import lombok.Getter;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

/**
 * Униформа, хранящая в себе параметр в виде буффера, который может быть разложен в программе на несколько данных.
 *
 * @see GlUniform
 * @see UniformType
 */
public class BufferUniform extends GlUniform {
    /** Индекс буффера униформы. **/
    @Getter
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
     * Устанавливает буффер из GpuBuffer.
     *
     * @param gpuBuffer GpuBuffer.
     *
     * @see GpuBuffer
     */
    public void set(GpuBuffer gpuBuffer) {
        this.uploadRunnable = () -> GL32.glBindBufferBase(
                BufferTarget.UNIFORM_BUFFER.glId,
                this.bufferIndex,
                gpuBuffer.getId()
        );
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает буффер при помощи пользовательского установщика.
     *
     * @param uploadConsumer установщик буффера.
     * @param buffer объект буффера.
     * @param <T> буффер.
     */
    public <T> void set(BiConsumer<BufferUniform, T> uploadConsumer, T buffer) {
        this.uploadRunnable = () -> uploadConsumer.accept(this, buffer);
        this.program.addUpdatedUniform(this);
    }

    @Override
    public void upload() {
        if (this.uploadRunnable != null)
            this.uploadRunnable.run();
    }
}
