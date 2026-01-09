package com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.AbstractMinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.RectColors;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.program.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
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
        super(AbstractMinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE_COLOR, Mesh.builder(DrawMode.QUADS, CustomVertexFormats.POSITION_TEXTURE_COLOR));
    }

    public ColoredTextureDrawer setTexture(int textureId) {
        SamplerUniform uniform = this.program.getSampler(0);
        this.uploadRunnable = () -> uniform.set(textureId);

        return this;
    }

    public ColoredTextureDrawer setTexture(GlTex texture) {
        SamplerUniform uniform = this.program.getSampler(0);
        this.uploadRunnable = () -> uniform.set(texture);

        return this;
    }

    public <T> ColoredTextureDrawer setTexture(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        SamplerUniform uniform = this.program.getSampler(0);
        this.uploadRunnable = () -> uniform.set(uploadConsumer, texture);

        return this;
    }

    public ColoredTextureDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, rectColors, textureBorder);
    }

    public ColoredTextureDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, rectColors, textureBorder, matrix4f);
    }

    public ColoredTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, TextureBorder textureBorder) {
        this.meshBuilder.vertex(x1, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    public ColoredTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(matrix4f, x1, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(matrix4f, x2, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(matrix4f, x2, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.initShaderColor();
        AbstractMinecraftPlugin.getInstance().initMatrix();

        if (this.uploadRunnable != null)
            this.uploadRunnable.run();

        CometRenderer.draw(this.mesh, false);
    }
}
