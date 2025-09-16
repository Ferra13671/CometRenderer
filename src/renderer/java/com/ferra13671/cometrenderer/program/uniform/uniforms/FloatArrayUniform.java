package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL20;

public class FloatArrayUniform extends GlUniform {
    private float[] value;

    public FloatArrayUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    public void set(float[] value) {
        this.value = value;
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform1fv(getLocation(), value);
    }
}
