package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MinecraftBufferRenderer {
    private final BufferRenderer<BufferBuilder.RenderedBuffer> renderer = (renderedBuffer, close) -> {
        BufferBuilder.DrawState drawState = renderedBuffer.drawState();

        if (drawState.indexCount() > 0) {
            drawState.format().getImmediateDrawVertexBuffer().upload(renderedBuffer);
            drawState.format().getImmediateDrawVertexBuffer().draw();
        }
    };

    public void draw(BufferBuilder.RenderedBuffer renderedBuffer) {
        CometRenderer.draw(renderer, renderedBuffer, false);
    }
}
