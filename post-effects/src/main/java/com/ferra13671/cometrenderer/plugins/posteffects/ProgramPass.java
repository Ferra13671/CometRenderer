package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.gltextureutils.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProgramPass {
    private final GlProgram program;
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs;
    private final Function<PostEffectContext, Framebuffer> output;
    private final Consumer<GlProgram> preRenderConsumer;

    public ProgramPass(GlProgram program, List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs, Function<PostEffectContext, Framebuffer> output, Consumer<GlProgram> preRenderConsumer) {
        this.program = program;
        this.inputs = inputs;
        this.output = output;
        this.preRenderConsumer = preRenderConsumer;
    }

    public void execute(PostEffectContext context) {
        Framebuffer outputFrameBuffer = output.apply(context);
        outputFrameBuffer.bind(true);

        CometRenderer.setGlobalProgram(program);
        for (Pair<Integer, Function<PostEffectContext, Framebuffer>> input : inputs)
            program.getSampler(input.getLeft()).set(input.getRight().apply(context).getColorTextureId());
        preRenderConsumer.accept(program);

        CometRenderer.draw(context.mesh(), false);
    }

    public static ProgramPassBuilder builder() {
        return new ProgramPassBuilder();
    }
}
