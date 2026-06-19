package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.batch.AbstractPrimitiveBatch;
import com.ferra13671.cometrenderer.minecraft.blur.BlurProvider;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.8")
public class RoundedBlurBatch extends AbstractPrimitiveBatch {
    private final BlurProvider blurProvider;

    public RoundedBlurBatch(BlurProvider blurProvider, Runnable preDrawRunnable) {
        this(blurProvider);
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedBlurBatch(BlurProvider blurProvider) {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.ROUNDED_BLUR));
        this.blurProvider = blurProvider;
    }

    public RoundedBlurBatch(BlurProvider blurProvider, int allocatorSize, Runnable preDrawRunnable) {
        this(blurProvider, allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedBlurBatch(BlurProvider blurProvider, int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.ROUNDED_BLUR));
        this.blurProvider = blurProvider;
    }

    public RoundedBlurBatch rectSized(float x, float y, float width, float height, float radius) {
        return rectPositioned(x, y, x + width, y + height, radius);
    }

    public RoundedBlurBatch rectPositioned(float x1, float y1, float x2, float y2, float radius) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 2;
        x2 += 2;
        y1 -= 2;
        y2 += 2;

        this.meshBuilder.vertex(x1, y1, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setCurrentProgram(CRM.getPrograms().ROUNDED_BLUR);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        CometRenderer.getCurrentProgram().getSampler(0).set(this.blurProvider.getFramebuffer().getColorTextureId());

        CometRenderer.draw(this.mesh, false);
    }
}
