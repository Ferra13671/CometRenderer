package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/*
 * Униформа, хранящая в себе mat4 значение
 */
public class Matrix4fGlUniform extends GlUniform {
    private final FloatBuffer buffer = MemoryUtil.memAllocFloat(16);

    public Matrix4fGlUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);
    }

    public void set(Matrix4f matrix4f) {
        matrix4f.get(buffer);
    }

    @Override
    @OverriddenMethod
    public void upload() {
        GL20.glUniformMatrix4fv(getLocation(), false, buffer);
    }
}
