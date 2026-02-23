package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.batch.AbstractPrimitiveBatch;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class BasicTextureBatch extends AbstractPrimitiveBatch {
    private Runnable uploadRunnable = null;

    public BasicTextureBatch(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicTextureBatch() {
        super(Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE));
    }

    public BasicTextureBatch(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicTextureBatch(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE));
    }

    public BasicTextureBatch setTexture(int textureId) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(textureId);

        return this;
    }

    public BasicTextureBatch setTexture(GlTex texture) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(texture);

        return this;
    }

    public <T> BasicTextureBatch setTexture(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(uploadConsumer, texture);

        return this;
    }

    public BasicTextureBatch rectSized(float x, float y, float width, float height, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, textureBorder);
    }

    public BasicTextureBatch rectSized(float x, float y, float width, float height, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, textureBorder, matrix4f);
    }

    public BasicTextureBatch rectPositioned(float x1, float y1, float x2, float y2, TextureBorder textureBorder) {
        this.meshBuilder.vertex(x1, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1());
        this.meshBuilder.vertex(x1, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2());
        this.meshBuilder.vertex(x2, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2());
        this.meshBuilder.vertex(x2, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1());

        return this;
    }

    public BasicTextureBatch rectPositioned(float x1, float y1, float x2, float y2, TextureBorder textureBorder, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1());
        this.meshBuilder.vertex(matrix4f, x1, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2());
        this.meshBuilder.vertex(matrix4f, x2, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2());
        this.meshBuilder.vertex(matrix4f, x2, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1());

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setCurrentProgram(CRM.getPrograms().POSITION_TEXTURE);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        if (this.uploadRunnable != null)
            this.uploadRunnable.run();

        CometRenderer.draw(this.mesh, false);
    }
}
