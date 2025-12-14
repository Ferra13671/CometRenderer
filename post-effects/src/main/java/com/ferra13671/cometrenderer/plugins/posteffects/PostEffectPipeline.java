package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import net.minecraft.client.gl.Framebuffer;

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
        HashMap<String, CometFrameBuffer> frameBuffers = new HashMap<>();
        for (LocalFrameBufferInfo info : localFrameBuffers) {
            CometFrameBuffer f = new CometFrameBuffer(info.name(), textureWidth, textureHeight, info.clearColor(), false);
            f.clearColorTexture();
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
