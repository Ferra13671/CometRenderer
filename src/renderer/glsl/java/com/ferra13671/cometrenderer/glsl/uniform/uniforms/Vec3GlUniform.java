package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GlUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде трёхмерного вектора с float значениями.
 *
 * @see GlUniform
 * @see UniformType
 */
public class Vec3GlUniform extends OneTypeGlUniform<Vector3f> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public Vec3GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform3f(getLocation(), this.value.x, this.value.y, this.value.z);
    }
}
