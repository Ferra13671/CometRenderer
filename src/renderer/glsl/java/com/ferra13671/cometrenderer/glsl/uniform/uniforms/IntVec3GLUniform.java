package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде трёхмерного вектора с int значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class IntVec3GLUniform extends OneTypeGLUniform<Vector3i> {

    public IntVec3GLUniform(String name, int location) {
        super(name, location);
    }

    @Override
    public void upload() {
        GL20.glUniform3i(getLocation(), this.value.x, this.value.y, this.value.z);
    }
}
