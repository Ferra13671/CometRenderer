package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * Униформа, хранящая в себе параметр в виде 4x4 float матрицы.
 *
 * @see GlUniform
 * @see UniformType
 */
public class Matrix4fGlUniform extends GlUniform {
    /** Буфер, в который будет записываться матрица. **/
    private final FloatBuffer buffer = MemoryUtil.memAllocFloat(16);

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public Matrix4fGlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    /**
     * Устанавливает матрицу в униформу.
     *
     * @param matrix4f матрица, которая будет записан как параметр в униформу.
     */
    public void set(Matrix4f matrix4f) {
        matrix4f.get(this.buffer);
        this.program.addUpdatedUniform(this);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniformMatrix4fv(getLocation(), false, this.buffer);
    }
}
