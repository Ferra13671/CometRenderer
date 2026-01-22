package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.gltextureutils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProgramPassBuilder extends Builder<ProgramPass> {
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs = new ArrayList<>();
    private Function<PostEffectContext, Framebuffer> output;
    private GlProgram program;
    private Consumer<GlProgram> preRenderConsumer = p -> {};

    public ProgramPassBuilder() {
        super("program pass");
    }

    public ProgramPassBuilder input(int samplerId, String frameBufferName) {
        this.inputs.add(new Pair<>(samplerId, context -> context.framebuffers().get(frameBufferName)));
        return this;
    }

    public ProgramPassBuilder input(int samplerId, Framebuffer framebuffer) {
        this.inputs.add(new Pair<>(samplerId, p -> framebuffer));
        return this;
    }

    public ProgramPassBuilder output(String frameBufferName) {
        this.output = context -> context.framebuffers().get(frameBufferName);
        return this;
    }

    public ProgramPassBuilder output(Framebuffer framebuffer) {
        this.output = p -> framebuffer;
        return this;
    }

    public ProgramPassBuilder program(GlProgram program) {
        this.program = program;
        return this;
    }

    public ProgramPassBuilder preRenderAction(Consumer<GlProgram> preRenderAction) {
        if (preRenderAction != null)
            this.preRenderConsumer = preRenderAction;
        return this;
    }

    @Override
    public ProgramPass build() {
        assertNotNull(this.output, "output");
        assertNotNull(this.program, "program");

        return new ProgramPass(this.program, this.inputs, this.output, this.preRenderConsumer);
    }
}
