package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.HasFramebuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import org.joml.Vector2f;

public class LiquidGlassBatch extends RoundedRectBatch {
    private final HasFramebuffer hasFramebuffer;

    public LiquidGlassBatch(HasFramebuffer hasFramebuffer, Runnable preDrawRunnable) {
        this(hasFramebuffer);
        this.preDrawRunnable = preDrawRunnable;
    }

    public LiquidGlassBatch(HasFramebuffer hasFramebuffer) {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.ROUNDED_RECT));

        this.hasFramebuffer = hasFramebuffer;
    }

    public LiquidGlassBatch(HasFramebuffer hasFramebuffer, int allocatorSize, Runnable preDrawRunnable) {
        this(hasFramebuffer, allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public LiquidGlassBatch(HasFramebuffer hasFramebuffer, int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.ROUNDED_RECT));

        this.hasFramebuffer = hasFramebuffer;
    }

    @Override
    protected void draw() {
        CometRenderer.setCurrentProgram(CRM.getPrograms().LIQUID_GLASS);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        Framebuffer framebuffer = this.hasFramebuffer.getFramebuffer();

        CometRenderer.getCurrentProgram().getSampler(0).set(framebuffer.getColorTextureId());
        CometRenderer.getCurrentProgram().getUniform("texelFetch", UniformType.VEC2).set(new Vector2f(1f / framebuffer.getWidth(), 1f / framebuffer.getHeight()));

        CometRenderer.draw(this.mesh, false);
    }
}
