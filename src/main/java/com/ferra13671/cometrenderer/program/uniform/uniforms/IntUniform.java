package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе int значение
 */
public class IntUniform extends OneTypeGlUniform<Integer> {

    public IntUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform1i(getLocation(), value);
    }
}
