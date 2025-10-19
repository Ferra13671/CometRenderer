package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе ivec3 значение
 */
public class IntVec3GlUniform extends OneTypeGlUniform<Vector3i> {

    public IntVec3GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform3i(getLocation(), value.x, value.y, value.z);
    }
}
