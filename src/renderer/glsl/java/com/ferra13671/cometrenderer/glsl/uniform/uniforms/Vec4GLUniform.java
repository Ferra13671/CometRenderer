package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде четырёхмерного вектора с float значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class Vec4GLUniform extends OneTypeGLUniform<Vector4f> {

    public Vec4GLUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform4f(getLocation(), this.value.x, this.value.y, this.value.z, this.value.w);
    }
}
