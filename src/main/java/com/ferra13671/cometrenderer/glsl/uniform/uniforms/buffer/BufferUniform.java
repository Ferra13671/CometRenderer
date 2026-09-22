package com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer;

import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindable;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindableBase;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindableRange;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL31;

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
    private UBOBindable bindable;

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

    public void set(GpuBuffer buffer) {
        set(new UBOBindableBase(buffer.getId()));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void set(GpuBuffer buffer, long offset, long size) {
        set(new UBOBindableRange(buffer.getId(), offset, size));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void set(UBOBindable bindable) {
        this.bindable = bindable;
    }

    @Override
    public void upload() {
        if (this.bindable != null)
            this.bindable.bind(getBufferBinding());
    }
}
