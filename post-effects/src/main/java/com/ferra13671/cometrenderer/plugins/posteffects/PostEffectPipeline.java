package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.vertex.DrawMode;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class PostEffectPipeline {
    private final List<LocalFrameBufferInfo> localFrameBuffers;
    private final List<ProgramPass> passes;

    public PostEffectPipeline(List<LocalFrameBufferInfo> localFrameBuffers, List<ProgramPass> passes) {
        this.localFrameBuffers = localFrameBuffers;
        this.passes = passes;
    }

    public void execute(int textureWidth, int textureHeight) {
        HashMap<String, Framebuffer> frameBuffers = new HashMap<>();
        for (LocalFrameBufferInfo info : localFrameBuffers) {
            FramebufferImpl f = new FramebufferImpl(info.name(), false, textureWidth, textureHeight, new Color(info.clearColor()), 0);
            f.clearColor();
            frameBuffers.put(info.name(), f);
        }

        PostEffectContext context = new PostEffectContext(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION, builder -> {
            builder.vertex(0f, textureHeight, 0f);
            builder.vertex(textureWidth, textureHeight, 0f);
            builder.vertex(textureWidth, 0, 0f);
            builder.vertex(0f, 0f, 0f);
        }), frameBuffers);

        for (ProgramPass pass : passes)
            pass.execute(context);

        context.mesh().close();
        for (Framebuffer f : context.framebuffers().values())
            f.delete();
    }

    public static PostEffectPipelineBuilder builder() {
        return new PostEffectPipelineBuilder();
    }
}
