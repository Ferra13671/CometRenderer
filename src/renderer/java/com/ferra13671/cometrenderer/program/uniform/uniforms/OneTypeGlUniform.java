package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;

public abstract class OneTypeGlUniform<T> extends GlUniform {
    protected T value = null;

    public OneTypeGlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    public void set(T value) {
        this.value = value;
    }
}
