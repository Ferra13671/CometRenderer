package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.apiguardian.api.API;
import org.joml.Matrix3fc;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * Униформа, хранящая в себе параметр в виде 3x3 float матрицы.
 *
 * @see GLUniform
 * @see UniformType
 */
@API(status = API.Status.MAINTAINED, since = "3.1")
public class Matrix3FGLUniform extends GLUniform {
    /** Буфер, в который будет записываться матрица. **/
    private final FloatBuffer buffer = MemoryUtil.memAllocFloat(9);

    public Matrix3FGLUniform(String name, int location) {
        super(name, location);
    }

    /**
     * Устанавливает матрицу в униформу.
     *
     * @param matrix матрица, которая будет записан как параметр в униформу.
     */
    public void set(Matrix3fc matrix) {
        matrix.get(this.buffer);
        this.program.addUpdatedUniform(this);
    }

    @Override
    public void upload() {
        GL20.glUniformMatrix3fv(getLocation(), false, this.buffer);
    }
}
