package com.ferra13671.cometrenderer.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class ColoredTextureDrawer extends AbstractDrawer {
    private Runnable uploadRunnable = null;

    public ColoredTextureDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public ColoredTextureDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.POSITION_TEXTURE_COLOR));
    }

    public ColoredTextureDrawer(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public ColoredTextureDrawer(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.POSITION_TEXTURE_COLOR));
    }

    public ColoredTextureDrawer setTexture(int textureId) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE_COLOR.getSampler(0);
        this.uploadRunnable = () -> uniform.set(textureId);

        return this;
    }

    public ColoredTextureDrawer setTexture(GlTex texture) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE_COLOR.getSampler(0);
        this.uploadRunnable = () -> uniform.set(texture);

        return this;
    }

    public <T> ColoredTextureDrawer setTexture(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        SamplerUniform uniform = CRM.getPrograms().POSITION_TEXTURE_COLOR.getSampler(0);
        this.uploadRunnable = () -> uniform.set(uploadConsumer, texture);

        return this;
    }

    public ColoredTextureDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, rectColors, textureBorder, null);
    }

    public ColoredTextureDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, rectColors, textureBorder, matrix4f);
    }

    public ColoredTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x1, y1, x2, y2, rectColors, textureBorder, null);
    }

    public ColoredTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        vertex(x1, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        vertex(x1, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        vertex(x2, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        vertex(x2, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    private MeshBuilder vertex(float x, float y, Matrix4f matrix4f) {
        if (matrix4f != null)
            return this.meshBuilder.vertex(matrix4f, x, y, 0);
        else return this.meshBuilder.vertex(x, y, 0);
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(CRM.getPrograms().POSITION_TEXTURE_COLOR);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        if (this.uploadRunnable != null)
            this.uploadRunnable.run();

        CometRenderer.draw(this.mesh, false);
    }
}
