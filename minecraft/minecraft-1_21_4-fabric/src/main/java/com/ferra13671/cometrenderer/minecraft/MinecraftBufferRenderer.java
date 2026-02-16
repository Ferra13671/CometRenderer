package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.vertex.MeshData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MinecraftBufferRenderer {
    private final BufferRenderer<MeshData> renderer = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            drawState.format().getImmediateDrawVertexBuffer().upload(builtBuffer);
            drawState.format().getImmediateDrawVertexBuffer().draw();
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
