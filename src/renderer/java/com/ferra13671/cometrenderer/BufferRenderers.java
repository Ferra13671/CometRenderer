package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.ShapeIndexBuffer;
import com.ferra13671.cometrenderer.vertex.builder.BuiltVertexBuffer;
import com.ferra13671.cometrenderer.vertex.format.uploader.VertexFormatManager;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public final class BufferRenderers {
    private static final ShapeIndexBuffer sharedSequential = new ShapeIndexBuffer(1, 1, IntConsumer::accept);
    private static final ShapeIndexBuffer sharedSequentialQuad = new ShapeIndexBuffer(4, 6, (indexConsumer, firstVertexIndex) -> {
        indexConsumer.accept(firstVertexIndex);
        indexConsumer.accept(firstVertexIndex + 1);
        indexConsumer.accept(firstVertexIndex + 2);
        indexConsumer.accept(firstVertexIndex + 2);
        indexConsumer.accept(firstVertexIndex + 3);
        indexConsumer.accept(firstVertexIndex);
    });

    public static final BiConsumer<BuiltBuffer, Boolean> MINECRAFT_BUFFER = (builtBuffer, close) -> {
        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();

        if (drawParameters.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawParameters.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex buffer", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            ((GlBackend) RenderSystem.getDevice()).getVertexBufferManager().setupBuffer(drawParameters.format(), (GlGpuBuffer) vertexBuffer);
            drawIndexed(drawParameters.indexCount(), GlConst.toGl(drawParameters.mode()), GlConst.toGl(indexType), indexBuffer);

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();
    };
    public static final BiConsumer<BuiltVertexBuffer, Boolean> COMET_BUFFER = (builtBuffer, close) -> {
        if (builtBuffer.getIndexCount() > 0) {

            ShapeIndexBuffer shapeIndexBuffer = getSequentialBuffer(builtBuffer.getDrawMode());
            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex buffer", 40, builtBuffer.getVertexBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(builtBuffer.getIndexCount());

            VertexFormatManager.applyFormatToBuffer((GlGpuBuffer) vertexBuffer, builtBuffer.getVertexFormat());
            drawIndexed(builtBuffer.getIndexCount(), builtBuffer.getDrawMode().glId, shapeIndexBuffer.getIndexType().glId, indexBuffer);

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();
    };

    private static ShapeIndexBuffer getSequentialBuffer(DrawMode drawMode) {
        return drawMode == DrawMode.QUADS ? sharedSequentialQuad : sharedSequential;
    }

    private static void drawIndexed(int count, int drawMode, int indexType, GpuBuffer indexBuffer) {
        GlStateManager._glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, CometRenderer.getBufferIdGetter().apply((GlGpuBuffer) indexBuffer));

        GlStateManager._drawElements(drawMode, count, indexType, 0);
    }
}
