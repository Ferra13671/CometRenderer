package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.test.mixins.IProjectionMatrix2;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.ProjectionMatrix2;
import org.joml.Matrix4f;

public class RenderUtils implements Mc {
    private static final ProjectionMatrix2 matrix = new ProjectionMatrix2("bthack-projection-matrix", -1000, 1000, true);
    public static Matrix4f projectionMatrix = new Matrix4f();

    /*
     * Убирает майнкрафтовский масштаб гуи и устанавливает кастомный
     */
    public static void unscaledProjection() {
        float width = mc.getWindow().getFramebufferWidth() / (float) 2;
        float height = mc.getWindow().getFramebufferHeight() / (float) 2;

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.ORTHOGRAPHIC);
        projectionMatrix.set(((IProjectionMatrix2) matrix)._getMatrix(width, height));
    }

    /*
     * Возвращает масштаб обратно
     */
    public static void scaledProjection() {
        float width = (float) (mc.getWindow().getFramebufferWidth() / 2f);
        float height = (float) (mc.getWindow().getFramebufferHeight() / 2f);

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.PERSPECTIVE);
        projectionMatrix.set(((IProjectionMatrix2) matrix)._getMatrix(width, height));
    }
}
