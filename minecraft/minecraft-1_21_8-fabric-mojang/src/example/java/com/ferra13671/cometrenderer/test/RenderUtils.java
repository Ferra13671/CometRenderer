package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.test.mixins.ICachedOrthoProjectionMatrixBuffer;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.joml.Matrix4f;

public class RenderUtils implements Mc {
    private static final CachedOrthoProjectionMatrixBuffer matrix = new CachedOrthoProjectionMatrixBuffer("bthack-projection-matrix", -1000, 1000, true);
    public static Matrix4f projectionMatrix = new Matrix4f();

    public static void unscaledProjection() {
        float width = mc.getWindow().getWidth() / 2f;
        float height = mc.getWindow().getHeight() / 2f;

        RenderSystem.setProjectionMatrix(matrix.getBuffer(width, height), ProjectionType.ORTHOGRAPHIC);
        projectionMatrix.set(((ICachedOrthoProjectionMatrixBuffer) matrix)._createProjectionMatrix(width, height));
    }

    public static void scaledProjection() {
        float width = mc.getWindow().getWidth() / 2f;
        float height = mc.getWindow().getHeight() / 2f;

        RenderSystem.setProjectionMatrix(matrix.getBuffer(width, height), ProjectionType.PERSPECTIVE);
        projectionMatrix.set(((ICachedOrthoProjectionMatrixBuffer) matrix)._createProjectionMatrix(width, height));
    }
}
