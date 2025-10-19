package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе ivec4 значение
 */
public class IntVec4GlUniform extends OneTypeGlUniform<Vector4i> {

    public IntVec4GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform4i(getLocation(), value.x, value.y, value.z, value.w);
    }
}
