package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Matrix4fGlUniform;
import com.ferra13671.cometrenderer.test.mixins.ProjectionMatrix2Accessor;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.ProjectionMatrix2;
import org.joml.Matrix4f;

public class RenderUtils implements Mc {
    private static final ProjectionMatrix2 matrix = new ProjectionMatrix2("bthack-projection-matrix", -1000, 1000, true);
    public static Matrix4f projectionMatrix = new Matrix4f();

    public static void setMatrixUniforms(GlProgram program) {
        BufferUniform projectionUniform = program.getUniform("Projection", BufferUniform.class);
        if (projectionUniform != null) {
            GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
            projectionUniform.set(slice);
        }

        Matrix4fGlUniform modelViewUniform = program.getUniform("modelViewMat", Matrix4fGlUniform.class);
        if (modelViewUniform != null)
            modelViewUniform.set(RenderSystem.getModelViewMatrix());
    }

    /*
     * Убирает майнкрафтовский масштаб гуи и устанавливает кастомный
     */
    public static void unscaledProjection() {
        float width = mc.getWindow().getFramebufferWidth() / (float) 2;
        float height = mc.getWindow().getFramebufferHeight() / (float) 2;

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.ORTHOGRAPHIC);
        projectionMatrix.set(((ProjectionMatrix2Accessor) matrix)._getMatrix(width, height));
    }

    /*
     * Возвращает масштаб обратно
     */
    public static void scaledProjection() {
        float width = (float) (mc.getWindow().getFramebufferWidth() / 2f);
        float height = (float) (mc.getWindow().getFramebufferHeight() / 2f);

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.PERSPECTIVE);
        projectionMatrix.set(((ProjectionMatrix2Accessor) matrix)._getMatrix(width, height));
    }
}
