package com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.AbstractMinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexElementTypes;
import com.ferra13671.cometrenderer.plugins.minecraft.CustomVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.RectColors;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.AbstractDrawer;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import org.joml.Matrix4f;

public class RoundedRectDrawer extends AbstractDrawer {

    public RoundedRectDrawer(Runnable preDrawRunnable) {
        this();
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedRectDrawer() {
        super(Mesh.builder(DrawMode.QUADS, CustomVertexFormats.ROUNDED_RECT));
    }

    public RoundedRectDrawer(int allocatorSize, Runnable preDrawRunnable) {
        this(allocatorSize);
        this.preDrawRunnable = preDrawRunnable;
    }

    public RoundedRectDrawer(int allocatorSize) {
        super(Mesh.builder(allocatorSize, DrawMode.QUADS, CustomVertexFormats.ROUNDED_RECT));
    }

    public RoundedRectDrawer rectSized(float x, float y, float width, float height, float radius, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, null);
    }

    public RoundedRectDrawer rectSized(float x, float y, float width, float height, float radius, RectColors rectColors, Matrix4f matrix4f) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors, matrix4f);
    }

    public RoundedRectDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors) {
        return rectPositioned(x1, y1, x2, y2, radius, rectColors, null);
    }

    public RoundedRectDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors, Matrix4f matrix4f) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 2;
        x2 += 2;
        y1 -= 2;
        y2 += 2;

        vertex(x1, y1, matrix4f)
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x1, y2, matrix4f)
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y2, matrix4f)
                .element("Color", CustomVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        vertex(x2, y1, matrix4f)
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
        CometRenderer.setGlobalProgram(AbstractMinecraftPlugin.getInstance().getPrograms().ROUNDED_RECT);

        CometRenderer.initShaderColor();
        AbstractMinecraftPlugin.getInstance().initMatrix();
        CometRenderer.getGlobalProgram().getUniform("height", UniformType.FLOAT).set((float) AbstractMinecraftPlugin.getInstance().getMainFramebufferHeight());

        CometRenderer.draw(this.mesh, false);
    }
}
