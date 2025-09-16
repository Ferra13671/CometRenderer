package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL20;

public class IntArrayUniform extends GlUniform {
    private int[] value;

    public IntArrayUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    public void set(int[] value) {
        this.value = value;
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform1iv(getLocation(), value);
    }
}
