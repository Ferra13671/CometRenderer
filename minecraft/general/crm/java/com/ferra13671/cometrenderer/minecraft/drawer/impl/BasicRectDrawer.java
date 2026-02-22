package com.ferra13671.cometrenderer.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.joml.Matrix4f;

public class BasicRectDrawer extends AbstractDrawer {

    public BasicRectDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicRectDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION));
    }

    public BasicRectDrawer(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicRectDrawer(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CometVertexFormats.POSITION));
    }

    public BasicRectDrawer rectSized(float x, float y, float width, float height) {
        return rectPositioned(x, y, x + width, y + height);
    }

    public BasicRectDrawer rectSized(float x, float y, float width, float height, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, matrix4f);
    }

    public BasicRectDrawer rectPositioned(float x1, float y1, float x2, float y2) {
        this.meshBuilder.vertex(x1, y1, 0);
        this.meshBuilder.vertex(x1, y2, 0);
        this.meshBuilder.vertex(x2, y2, 0);
        this.meshBuilder.vertex(x2, y1, 0);

        return this;
    }

    public BasicRectDrawer rectPositioned(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0);
        this.meshBuilder.vertex(matrix4f, x1, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y1, 0);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(CRM.getPrograms().POSITION);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        CometRenderer.draw(this.mesh, false);
    }
}
