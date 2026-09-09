package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе int параметр.
 *
 * @see GLUniform
 * @see UniformType
 */
public class IntUniform extends OneTypeGLUniform<Integer> {

    public IntUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform1i(getLocation(), this.value);
    }
}
