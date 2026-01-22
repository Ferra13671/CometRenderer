package com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.AbstractMinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.RectColors;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class RoundedTextureDrawer extends AbstractDrawer {
    private Runnable uploadRunnable = null;

    public RoundedTextureDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedTextureDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.ROUNDED_TEXTURE));
    }

    public RoundedTextureDrawer(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedTextureDrawer(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.ROUNDED_TEXTURE));
    }

    public RoundedTextureDrawer setTexture(int textureId) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().ROUNDED_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(textureId);

        return this;
    }

    public RoundedTextureDrawer setTexture(GlTex texture) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().ROUNDED_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(texture);

        return this;
    }

    public <T> RoundedTextureDrawer setTexture(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().ROUNDED_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(uploadConsumer, texture);

        return this;
    }

    public RoundedTextureDrawer rectSized(float x, float y, float width, float height, float radius, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, textureBorder, null);
    }

    public RoundedTextureDrawer rectSized(float x, float y, float width, float height, float radius, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, textureBorder, matrix4f);
    }

    public RoundedTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x1, y1, x2, y2, radius, rectColors, textureBorder, null);
    }

    public RoundedTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 2;
        x2 += 2;
        y1 -= 2;
        y2 += 2;

        vertex(x1, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x1, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);

        return this;
    }

    private MeshBuilder vertex(float x, float y, Matrix4f matrix4f) {
        if (matrix4f != null)
            return this.meshBuilder.vertex(matrix4f, x, y, 0);
        else return this.meshBuilder.vertex(x, y, 0);
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(AbstractMinecraftPlugin.getInstance().getPrograms().ROUNDED_TEXTURE);

        CometRenderer.initShaderColor();
        AbstractMinecraftPlugin.getInstance().initMatrix();
        CometRenderer.getGlobalProgram().getUniform("height", UniformType.FLOAT).set((float) AbstractMinecraftPlugin.getInstance().getMainFramebufferHeight());

        if (this.uploadRunnable != null)
            this.uploadRunnable.run();

        CometRenderer.draw(this.mesh, false);
    }
}
