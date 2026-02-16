package com.ferra13671.cometrenderer.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.joml.Matrix4f;

public class ColoredRectDrawer extends AbstractDrawer {

    public ColoredRectDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public ColoredRectDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.POSITION_COLOR));
    }

    public ColoredRectDrawer(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public ColoredRectDrawer(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.POSITION_COLOR));
    }

    public ColoredRectDrawer rectSized(float x, float y, float width, float height, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, rectColors);
    }

    public ColoredRectDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, rectColors, matrix4f);
    }

    public ColoredRectDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors) {
        this.meshBuilder.vertex(x1, y1, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(x1, y2, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(x2, y2, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(x2, y1, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    public ColoredRectDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(matrix4f, x1, y2, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(matrix4f, x2, y2, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(matrix4f, x2, y1, 0).element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(CRM.getPrograms().POSITION_COLOR);

        CometRenderer.initShaderColor();
        CRM.initMatrix();

        CometRenderer.draw(this.mesh, false);
    }
}
