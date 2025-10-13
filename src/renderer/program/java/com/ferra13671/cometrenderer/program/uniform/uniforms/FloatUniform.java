package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе float значение
 */
public class FloatUniform extends OneTypeGlUniform<Float> {

    public FloatUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform1f(getLocation(), value);
    }
}
