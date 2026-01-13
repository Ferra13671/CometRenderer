package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде двумерного вектора с int значениями.
 *
 * @see GlUniform
 * @see UniformType
 */
public class IntVec2GlUniform extends OneTypeGlUniform<Vector2i> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public IntVec2GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform2i(getLocation(), this.value.x, this.value.y);
    }
}
