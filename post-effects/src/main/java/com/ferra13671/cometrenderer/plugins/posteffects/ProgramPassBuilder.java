package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramPassBuilderArgumentException;
import com.ferra13671.cometrenderer.program.GlProgram;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProgramPassBuilder {
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs = new ArrayList<>();
    private Function<PostEffectContext, Framebuffer> output;
    private GlProgram program;
    private Consumer<GlProgram> preRenderConsumer = program -> {};

    public ProgramPassBuilder input(int samplerId, String frameBufferName) {
        this.inputs.add(new Pair<>(samplerId, context -> context.framebuffers().get(frameBufferName)));
        return this;
    }

    public ProgramPassBuilder input(int samplerId, Framebuffer framebuffer) {
        this.inputs.add(new Pair<>(samplerId, context -> framebuffer));
        return this;
    }

    public ProgramPassBuilder output(String frameBufferName) {
        this.output = context -> context.framebuffers().get(frameBufferName);
        return this;
    }

    public ProgramPassBuilder output(Framebuffer framebuffer) {
        this.output = context -> framebuffer;
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

    public ProgramPass build() {
        if (output == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing output in program pipeline."));
        if (program == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing program in program pipeline."));

        return new ProgramPass(program, inputs, output, preRenderConsumer);
    }
}
