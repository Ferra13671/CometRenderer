package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде четырёхмерного вектора с int значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class IntVec4GLUniform extends OneTypeGLUniform<Vector4i> {

    public IntVec4GLUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform4i(getLocation(), this.value.x, this.value.y, this.value.z, this.value.w);
    }
}
