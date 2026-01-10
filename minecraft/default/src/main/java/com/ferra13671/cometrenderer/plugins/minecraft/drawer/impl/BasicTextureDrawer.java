package com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.AbstractMinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.program.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class BasicTextureDrawer extends AbstractDrawer {
    private Runnable uploadRunnable = null;

    public BasicTextureDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public BasicTextureDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE));
    }

    public BasicTextureDrawer setTexture(int textureId) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(textureId);

        return this;
    }

    public BasicTextureDrawer setTexture(GlTex texture) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(texture);

        return this;
    }

    public <T> BasicTextureDrawer setTexture(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        SamplerUniform uniform = AbstractMinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE.getSampler(0);
        this.uploadRunnable = () -> uniform.set(uploadConsumer, texture);

        return this;
    }

    public BasicTextureDrawer rectSized(float x, float y, float width, float height, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, textureBorder);
    }

    public BasicTextureDrawer rectSized(float x, float y, float width, float height, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, textureBorder, matrix4f);
    }

    public BasicTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, TextureBorder textureBorder) {
        this.meshBuilder.vertex(x1, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1());
        this.meshBuilder.vertex(x1, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2());
        this.meshBuilder.vertex(x2, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2());
        this.meshBuilder.vertex(x2, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1());

        return this;
    }

    public BasicTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, TextureBorder textureBorder, Matrix4f matrix4f) {
        this.meshBuilder.vertex(matrix4f, x1, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1());
        this.meshBuilder.vertex(matrix4f, x1, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2());
        this.meshBuilder.vertex(matrix4f, x2, y2, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2());
        this.meshBuilder.vertex(matrix4f, x2, y1, 0).element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1());

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(AbstractMinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE);

        CometRenderer.initShaderColor();
        AbstractMinecraftPlugin.getInstance().initMatrix();

        if (this.uploadRunnable != null)
            this.uploadRunnable.run();

        CometRenderer.draw(this.mesh, false);
    }
}
