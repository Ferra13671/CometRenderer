package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде двумерного вектора с float значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class Vec2GLUniform extends OneTypeGLUniform<Vector2f> {

    public Vec2GLUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform2f(getLocation(), this.value.x, this.value.y);
    }
}
