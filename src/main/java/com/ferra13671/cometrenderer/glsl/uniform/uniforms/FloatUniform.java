package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе float параметр.
 *
 * @see GLUniform
 * @see UniformType
 */
public class FloatUniform extends OneTypeGLUniform<Float> {

    public FloatUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform1f(getLocation(), this.value);
    }
}
