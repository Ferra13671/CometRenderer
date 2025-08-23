package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.UniformException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlConst;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

/*
 * Униформа, хранящая в себе буффер, который в последствии разбивается на другие юниформы
 */
public class BufferUniform extends OneTypeGlUniform<GpuBufferSlice> {
    private final int blockBinding;

    public BufferUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        int index = GL31.glGetUniformBlockIndex(glProgram.getId(), name);
        if (index == -1) {
            throw new UniformException(String.format("Cannot find buffer uniform '%s' in program '%s'.", name, glProgram.getName()));
        } else {
            blockBinding = glProgram.getBuffersBindingsAmount() + 1;
            glProgram.setBuffersBindingsAmount(blockBinding);
            GL31.glUniformBlockBinding(glProgram.getId(), index, blockBinding);
        }
    }

    public int getBlockBinding() {
        return blockBinding;
    }

    @Override
    public void upload() {
        GL32.glBindBufferRange(GlConst.GL_UNIFORM_BUFFER, getBlockBinding(), CometRenderer.getBufferIdGetter().apply((GlGpuBuffer) value.buffer()), value.offset(), value.length());
    }
}
