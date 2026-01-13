package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе int параметр.
 *
 * @see GlUniform
 * @see UniformType
 */
public class IntUniform extends OneTypeGlUniform<Integer> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public IntUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform1i(getLocation(), this.value);
    }
}
