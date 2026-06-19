package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.batch.AbstractPrimitiveBatch;
import com.ferra13671.cometrenderer.minecraft.blur.BlurProvider;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.apiguardian.api.API;
import org.joml.Matrix4f;

@API(status = API.Status.MAINTAINED, since = "2.2")
public class BasicBlurBatch extends AbstractPrimitiveBatch {
    private final BlurProvider blurProvider;

    public BasicBlurBatch(BlurProvider blurProvider, Runnable preDrawRunnable) {
        this(blurProvider);
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicBlurBatch(BlurProvider blurProvider) {
        super(Mesh.builder(DrawMode.QUADS, VertexFormat.POSITION));
        this.blurProvider = blurProvider;
    }

    public BasicBlurBatch(BlurProvider blurProvider, int allocatorSize, Runnable preDrawRunnable) {
        this(blurProvider, allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicBlurBatch(BlurProvider blurProvider, int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, VertexFormat.POSITION));
        this.blurProvider = blurProvider;
    }

    public BasicBlurBatch rectSized(float x, float y, float width, float height) {
        return rectPositioned(x, y, x + width, y + height);
    }

    public BasicBlurBatch rectSized(float x, float y, float width, float height, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, matrix4f);
    }

    public BasicBlurBatch rectPositioned(float x1, float y1, float x2, float y2) {
        this.meshBuilder.vertex(x1, y1, 0);
        this.meshBuilder.vertex(x1, y2, 0);
        this.meshBuilder.vertex(x2, y2, 0);
        this.meshBuilder.vertex(x2, y1, 0);

        return this;
    }

    public BasicBlurBatch rectPositioned(float x1, float y1, float x2, float y2, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0);
        this.meshBuilder.vertex(matrix4f, x1, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y2, 0);
        this.meshBuilder.vertex(matrix4f, x2, y1, 0);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setCurrentProgram(CRM.getPrograms().BLIT);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        CometRenderer.getCurrentProgram().getSampler(0).set(this.blurProvider.getFramebuffer().getColorTextureId());

        CometRenderer.draw(this.mesh, false);
    }
}
