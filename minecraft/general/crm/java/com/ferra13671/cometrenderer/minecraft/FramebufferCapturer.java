package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferInfo;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

import java.awt.*;

public class FramebufferCapturer implements HasFramebuffer {
    private final FramebufferImpl capturedFramebuffer = new FramebufferImpl(
            FramebufferInfo.builder()
                    .name("Captured framebuffer")
                    .useDepth(false)
                    .clearColor(Color.blue)
                    .width(1)
                    .height(1)
                    .build()
    );

    @Override
    public Framebuffer getFramebuffer() {
        return this.capturedFramebuffer;
    }

    public void capture(Framebuffer framebuffer) {
        FramebufferUtils.resizeToParent(this.capturedFramebuffer, framebuffer);

        this.capturedFramebuffer.bind(true);
        CometRenderer.setCurrentProgram(CRM.getPrograms().BLIT);
        CometRenderer.getCurrentProgram().getSampler(0).set(framebuffer.getColorTextureId());

        int width = framebuffer.getWidth();
        int height = framebuffer.getHeight();
        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, VertexFormat.POSITION, mesh ->
            mesh
                    .vertex(0, 0, 0)
                    .vertex(0, height, 0)
                    .vertex(width, height, 0)
                    .vertex(width, 0, 0)
        ));
    }
}
