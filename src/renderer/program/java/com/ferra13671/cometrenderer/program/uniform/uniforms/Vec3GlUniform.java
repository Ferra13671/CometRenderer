package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе vec3 значение
 */
public class Vec3GlUniform extends OneTypeGlUniform<Vector3f> {

    public Vec3GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniform3f(getLocation(), value.x, value.y, value.z);
    }
}
