package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе ivec2 значение
 */
public class IntVec2GlUniform extends OneTypeGlUniform<Vector2i> {

    public IntVec2GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform2i(getLocation(), value.x, value.y);
    }
}
