package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде двумерного вектора с int значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class IntVec2GLUniform extends OneTypeGLUniform<Vector2i> {

    public IntVec2GLUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform2i(getLocation(), this.value.x, this.value.y);
    }
}
