package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде float массива.
 *
 * @see GLUniform
 * @see UniformType
 */
public class FloatArrayUniform extends OneTypeGLUniform<float[]> {

    public FloatArrayUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform1fv(getLocation(), this.value);
    }
}
