package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде int массива.
 *
 * @see GlUniform
 * @see UniformType
 */
public class IntArrayUniform extends OneTypeGlUniform<int[]> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public IntArrayUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform1iv(getLocation(), this.value);
    }
}
