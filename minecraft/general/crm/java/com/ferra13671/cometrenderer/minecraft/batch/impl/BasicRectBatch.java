package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.batch.AbstractPrimitiveBatch;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.joml.Matrix4f;

public class BasicRectBatch extends AbstractPrimitiveBatch {

    public BasicRectBatch(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicRectBatch() {
        super(Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION));
    }

    public BasicRectBatch(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicRectBatch(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CometVertexFormats.POSITION));
    }

    public BasicRectBatch rectSized(float x, float y, float width, float height) {
        return rectPositioned(x, y, x + width, y + height);
    }

    public BasicRectBatch rectSized(float x, float y, float width, float height, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, matrix4f);
    }

    public BasicRectBatch rectPositioned(float x1, float y1, float x2, float y2) {
        this.meshBuilder.vertex(x1, y1, 0);
        this.meshBuilder.vertex(x1, y2, 0);
        this.meshBuilder.vertex(x2, y2, 0);
        this.meshBuilder.vertex(x2, y1, 0);

        return this;
    }

    public BasicRectBatch rectPositioned(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0);
        this.meshBuilder.vertex(matrix4f, x1, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y1, 0);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setCurrentProgram(CRM.getPrograms().POSITION);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        CometRenderer.draw(this.mesh, false);
    }
}
