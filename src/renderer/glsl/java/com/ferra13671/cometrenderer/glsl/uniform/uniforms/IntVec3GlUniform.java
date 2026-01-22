package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GlUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде трёхмерного вектора с int значениями.
 *
 * @see GlUniform
 * @see UniformType
 */
public class IntVec3GlUniform extends OneTypeGlUniform<Vector3i> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public IntVec3GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform3i(getLocation(), this.value.x, this.value.y, this.value.z);
    }
}
