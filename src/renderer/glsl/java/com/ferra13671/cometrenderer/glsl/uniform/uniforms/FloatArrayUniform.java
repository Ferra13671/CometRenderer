package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GlUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде float массива.
 *
 * @see GlUniform
 * @see UniformType
 */
public class FloatArrayUniform extends OneTypeGlUniform<float[]> {

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public FloatArrayUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform1fv(getLocation(), this.value);
    }
}
