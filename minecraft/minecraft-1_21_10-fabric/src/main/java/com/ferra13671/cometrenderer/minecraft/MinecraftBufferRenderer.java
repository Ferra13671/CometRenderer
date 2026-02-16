package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.mixins.IGlBuffer;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

@UtilityClass
public class MinecraftBufferRenderer {
    private final BufferRenderer<MeshData> renderer = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawState.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex mesh", 40, builtBuffer.vertexBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getBuffer(drawState.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.type();

            ((GlDevice) RenderSystem.getDevice()).vertexArrayCache().bindVertexArray(
                    drawState.format(),
                    (GlBuffer) vertexBuffer
            );
            GL15.glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, ((IGlBuffer) indexBuffer)._getHandle());
            GL11.glDrawElements(
                    GlConst.toGl(drawState.mode()),
                    drawState.indexCount(),
                    GlConst.toGl(indexType),
                    0
            );

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();
    };

    public void draw(MeshData meshData) {
        draw(meshData, true);
    }

    public void draw(MeshData meshData, boolean close) {
        CometRenderer.draw(renderer, meshData, close);
    }
}
