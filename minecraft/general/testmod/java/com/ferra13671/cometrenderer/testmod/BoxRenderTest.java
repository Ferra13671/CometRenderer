package com.ferra13671.cometrenderer.testmod;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.CustomDrawMode;
import com.ferra13671.cometrenderer.minecraft.CustomVertexElementTypes;

public class BoxRenderTest {
    private static final Matrix4f rotateMatrix = new Matrix4f();
    private static long lastRotateTime = System.currentTimeMillis();

    public static void draw(Vector3f position, RenderColor color) {
        CometRenderer.setGlobalProgram(CRM.getPrograms().POSITION_COLOR);
        CometRenderer.initShaderColor();
        CRM.initMatrix();

        MeshBuilder cubeBuilder = Mesh.builder(CustomDrawMode.CUBE, CustomVertexFormats.POSITION_COLOR);
        MeshBuilder cubeOutlineBuilder = Mesh.builder(CustomDrawMode.CUBE_OUTLINE, CustomVertexFormats.POSITION_COLOR);

        long timeMillis = System.currentTimeMillis();

        float rotate = (float) Math.toRadians(timeMillis - lastRotateTime);

        rotateMatrix.rotate(rotate * 0.2f, 1, 0, 0);
        rotateMatrix.rotate(rotate * 0.1f, 0, 1, 0);
        lastRotateTime = timeMillis;

        drawBox(cubeBuilder, position, color.multiply(RenderColor.of(1f, 1f, 1f, 0.3f)));
        drawBox(cubeOutlineBuilder, position, color);

        CometRenderer.draw(cubeBuilder.buildNullable());
        CometRenderer.draw(cubeOutlineBuilder.buildNullable());
    }

    private static void drawBox(MeshBuilder meshBuilder, Vector3f position, RenderColor color) {
        Vector3f[] positions = {
                new Vector3f(0.5f, -0.5f, -0.5f),
                new Vector3f(0.5f, -0.5f, 0.5f),
                new Vector3f(-0.5f, -0.5f, 0.5f),
                new Vector3f(-0.5f, -0.5f, -0.5f),

                new Vector3f(-0.5f, 0.5f, -0.5f),
                new Vector3f(-0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, -0.5f)
        };
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Vector3f(position).add(rotateMatrix.transformPosition(positions[i]));
        }

        meshBuilder.vertex(positions[0].x, positions[0].y, positions[0].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[1].x, positions[1].y, positions[1].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[2].x, positions[2].y, positions[2].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[3].x, positions[3].y, positions[3].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);

        meshBuilder.vertex(positions[4].x, positions[4].y, positions[4].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[5].x, positions[5].y, positions[5].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[6].x, positions[6].y, positions[6].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
        meshBuilder.vertex(positions[7].x, positions[7].y, positions[7].z).element("Color", CustomVertexElementTypes.RENDER_COLOR, color);
    }
}
