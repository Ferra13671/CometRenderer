package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде int массива.
 *
 * @see GLUniform
 * @see UniformType
 */
public class IntArrayUniform extends OneTypeGLUniform<int[]> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GLProgram}), к которой привязана униформа.
     */
    public IntArrayUniform(String name, int location, GLProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform1iv(getLocation(), this.value);
    }
}
