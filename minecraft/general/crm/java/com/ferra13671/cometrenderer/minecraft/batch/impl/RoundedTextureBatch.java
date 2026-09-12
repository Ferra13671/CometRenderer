package com.ferra13671.cometrenderer.minecraft.batch.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.batch.AbstractPrimitiveBatch;
import com.ferra13671.cometrenderer.sampler.GLSampler;
import com.ferra13671.cometrenderer.sampler.unit.SamplerUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.TextureUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.UnitBindable;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.apiguardian.api.API;
import org.joml.Matrix4f;

@API(status = API.Status.MAINTAINED, since = "2.2")
public class RoundedTextureBatch extends AbstractPrimitiveBatch {
    private UnitBindable textureBindable = TextureUnitBindable.EMPTY;
    private UnitBindable samplerBindable = SamplerUnitBindable.EMPTY;

    public RoundedTextureBatch(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedTextureBatch() {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.ROUNDED_TEXTURE));
    }

    public RoundedTextureBatch(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedTextureBatch(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.ROUNDED_TEXTURE));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch texture(GlTex texture) {
        return texture(texture == null ? 0 : texture.getTexId());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch texture(int textureId) {
        return texture(textureId == 0 ? TextureUnitBindable.EMPTY : new TextureUnitBindable(textureId));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch texture(UnitBindable bindable) {
        this.textureBindable = bindable;

        return this;
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch sampler(GLSampler sampler) {
        return sampler(sampler == null ? 0 : sampler.getId());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch sampler(int samplerId) {
        return sampler(samplerId == 0 ? SamplerUnitBindable.EMPTY : new SamplerUnitBindable(samplerId));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch sampler(UnitBindable bindable) {
        this.samplerBindable = bindable;

        return this;
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch textureSampler(GlTex texture, GLSampler sampler) {
        return texture(texture).sampler(sampler);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch textureSampler(int[] ids) {
        return textureSampler(ids[0], ids[1]);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch textureSampler(UnitBindable[] bindables) {
        return textureSampler(bindables[0], bindables[1]);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch textureSampler(int textureId, int samplerId) {
        return texture(textureId).sampler(samplerId);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public RoundedTextureBatch textureSampler(UnitBindable textureBindable, UnitBindable samplerBindable) {
        return texture(textureBindable).sampler(samplerBindable);
    }

    public RoundedTextureBatch rectSized(float x, float y, float width, float height, float radius, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, textureBorder, null);
    }

    public RoundedTextureBatch rectSized(float x, float y, float width, float height, float radius, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, textureBorder, matrix4f);
    }

    public RoundedTextureBatch rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x1, y1, x2, y2, radius, rectColors, textureBorder, null);
    }

    public RoundedTextureBatch rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors, TextureBorder textureBorder, Matrix4f matrix4f) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 2;
        x2 += 2;
        y1 -= 2;
        y2 += 2;

        vertex(x1, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.u1(), textureBorder.v1())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x1, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.u1(), textureBorder.v2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y2, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.u2(), textureBorder.v2())
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y1, matrix4f)
                .element("Texture", VertexElementType.FLOAT, textureBorder.u2(), textureBorder.v1())
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
        CometRenderer.setCurrentProgram(CRM.getPrograms().ROUNDED_TEXTURE);

        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();

        CometRenderer.getCurrentProgram().getSampler(0).setTextureSampler(this.textureBindable, this.samplerBindable);

        CometRenderer.draw(this.mesh, false);
    }
}
