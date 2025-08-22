package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

/*
 * Униформа, хранящая в себе vec4 значение
 */
public class Vec4GlUniform extends OneTypeGlUniform<Vector4f> {

    public Vec4GlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    @Override
    public void upload() {
        GL20.glUniform4f(getLocation(), value.x, value.y, value.z, value.w);
    }
}
