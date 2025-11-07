package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.vertex.ShapeIndexBuffer;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import com.ferra13671.cometrenderer.vertex.format.uploader.VertexFormatManager;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.function.BiConsumer;

/**
 * Все отрисовщики буфферов вершин, которые есть в CometRenderer.
 */
public final class BufferRenderers {

    public static final BiConsumer<BuiltBuffer, Boolean> MINECRAFT_BUFFER = (builtBuffer, close) -> {
        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();

        if (drawParameters.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawParameters.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex mesh", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            ((GlBackend) RenderSystem.getDevice()).getVertexBufferManager().setupBuffer(
                    drawParameters.format(),
                    (GlGpuBuffer) vertexBuffer
            );
            GL15.glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, CometRenderer.getBufferIdGetter().apply((GlGpuBuffer) indexBuffer));
            drawIndexed(
                    drawParameters.indexCount(),
                    GlConst.toGl(drawParameters.mode()),
                    GlConst.toGl(indexType)
            );

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();
    };
    public static final BiConsumer<IMesh, Boolean> COMET_BUFFER = (mesh, close) -> {
        if (mesh.getIndexCount() > 0) {
            ShapeIndexBuffer shapeIndexBuffer = mesh.getDrawMode().shapeIndexBuffer;

            VertexFormatManager.uploadFormatToBuffer(mesh.getVertexBuffer(), mesh.getVertexFormat());

            com.ferra13671.cometrenderer.buffer.GpuBuffer indexBuffer = mesh.getIndexBuffer();
            if (indexBuffer.getTarget() != BufferTarget.ELEMENT_ARRAY_BUFFER)
                ExceptionPrinter.printAndExit(new WrongGpuBufferTargetException(indexBuffer.getTarget().glId, BufferTarget.ELEMENT_ARRAY_BUFFER.glId));
            indexBuffer.bind();

            drawIndexed(mesh.getIndexCount(), mesh.getDrawMode().glId, shapeIndexBuffer.getIndexType().glId);
        }
        if (close)
            mesh.close();
    };

    private static void drawIndexed(int count, int drawMode, int indexType) {
        GL11.glDrawElements(drawMode, count, indexType, 0);
    }
}
