package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.gltextureutils.Pair;
import org.apiguardian.api.API;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@API(status = API.Status.MAINTAINED)
public class ProgramPass {
    private final GLProgram program;
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs;
    private final Function<PostEffectContext, Framebuffer> output;
    private final Consumer<GLProgram> preRenderConsumer;

    public ProgramPass(GLProgram program, List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs, Function<PostEffectContext, Framebuffer> output, Consumer<GLProgram> preRenderConsumer) {
        this.program = program;
        this.inputs = inputs;
        this.output = output;
        this.preRenderConsumer = preRenderConsumer;
    }

    public void execute(PostEffectContext context) {
        Framebuffer outputFrameBuffer = output.apply(context);
        outputFrameBuffer.bind(true);

        CometRenderer.setCurrentProgram(program);
        for (Pair<Integer, Function<PostEffectContext, Framebuffer>> input : inputs)
            program.getSampler(input.getLeft()).set(input.getRight().apply(context).getColorTextureId());
        preRenderConsumer.accept(program);

        CometRenderer.draw(context.mesh(), false);
    }

    public static ProgramPassBuilder builder() {
        return new ProgramPassBuilder();
    }
}
