package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде трёхмерного вектора с float значениями.
 *
 * @see GLUniform
 * @see UniformType
 */
public class Vec3GLUniform extends OneTypeGLUniform<Vector3f> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GLProgram}), к которой привязана униформа.
     */
    public Vec3GLUniform(String name, int location, GLProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform3f(getLocation(), this.value.x, this.value.y, this.value.z);
    }
}
