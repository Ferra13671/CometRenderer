package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
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
    @OverriddenMethod
    public void upload() {
        GL20.glUniform1fv(getLocation(), this.value);
    }
}
