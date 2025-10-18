package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramPassBuilderArgumentException;
import com.ferra13671.cometrenderer.program.GlProgram;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * Билдер "под эффекта" пост эффекта
 */
public class ProgramPassBuilder {
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs = new ArrayList<>();
    private Function<PostEffectContext, Framebuffer> output;
    private GlProgram program;
    private Consumer<GlProgram> preRenderAction = program -> {};

    /*
     * Добавляет входной фреймбуффер
     */
    public ProgramPassBuilder input(int samplerId, String frameBufferName) {
        this.inputs.add(new Pair<>(samplerId, context -> context.framebuffers().get(frameBufferName)));
        return this;
    }

    /*
     * Добавляет входной фреймбуффер
     */
    public ProgramPassBuilder input(int samplerId, Framebuffer framebuffer) {
        this.inputs.add(new Pair<>(samplerId, context -> framebuffer));
        return this;
    }

    /*
     * Устанавливает выходной фреймбуффер
     */
    public ProgramPassBuilder output(String frameBufferName) {
        this.output = context -> context.framebuffers().get(frameBufferName);
        return this;
    }

    /*
     * Устанавливает выходной фреймбуффер
     */
    public ProgramPassBuilder output(Framebuffer framebuffer) {
        this.output = context -> framebuffer;
        return this;
    }

    /*
     * Устанавливает программу
     */
    public ProgramPassBuilder program(GlProgram program) {
        this.program = program;
        return this;
    }

    /*
     * Устанавливает действие перед рендером
     */
    public ProgramPassBuilder preRenderAction(Consumer<GlProgram> preRenderAction) {
        if (preRenderAction != null)
            this.preRenderAction = preRenderAction;
        return this;
    }

    /*
     * Билдит "под эффект" пост эффекта
     */
    public ProgramPass build() {
        if (output == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing output in program pipeline."));
        if (program == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing program in program pipeline."));

        return new ProgramPass(program, inputs, output, preRenderAction);
    }
}
