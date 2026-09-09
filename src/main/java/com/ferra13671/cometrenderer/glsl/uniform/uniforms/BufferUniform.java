package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

import java.util.function.BiConsumer;

/**
 * Униформа, хранящая в себе параметр в виде буффера, который может быть разложен в программе на несколько данных.
 *
 * @see GLUniform
 * @see UniformType
 */
public class BufferUniform extends GLUniform {
    /** Индекс буффера униформы. **/
    @Getter
    private int bufferIndex;
    /** Номер биндинга буффера униформы. **/
    @Getter
    @Setter
    private int bufferBinding;
    /** Runnable, загружающий параметр в униформу. **/
    private Runnable uploadRunnable = null;

    public BufferUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void setProgram(GLProgram program) {
        super.setProgram(program);

        this.bufferIndex = GL31.glGetUniformBlockIndex(program.getId(), name);
        if (this.bufferIndex == -1)
            ErrorHandlers.onNoSuchUniform(name, program.getName());
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
                this.bufferBinding,
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
