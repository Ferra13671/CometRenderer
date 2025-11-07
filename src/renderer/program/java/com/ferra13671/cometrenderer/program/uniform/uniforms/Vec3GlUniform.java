package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
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
    @OverriddenMethod
    public void upload() {
        GL20.glUniform3f(getLocation(), this.value.x, this.value.y, this.value.z);
    }
}
