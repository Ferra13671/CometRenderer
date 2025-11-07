package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalProgramPassBuilderArgumentException;
import com.ferra13671.cometrenderer.posteffect.PostEffectContext;
import com.ferra13671.cometrenderer.posteffect.ProgramPass;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.posteffect.PostEffectPipeline;
import com.ferra13671.cometrenderer.posteffect.LocalFrameBufferInfo;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Сборщик пасса пост эффекта.
 *
 * @see ProgramPass
 * @see PostEffectPipeline
 */
public class ProgramPassBuilder {
    /** Список входных фреймбуфферов, привязанных к семплерам программы. **/
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs = new ArrayList<>();
    /** Выходной фреймбуффер пасса. **/
    private Function<PostEffectContext, Framebuffer> output;
    /** Программа, которая будет использоваться пассом. **/
    private GlProgram program;
    /** Действие, выполняемое перед выполнением программы. **/
    private Consumer<GlProgram> preRenderConsumer = program -> {};

    /**
     * Добавляет входной фреймбуффер в пасс.
     *
     * @param samplerId айди семплера в программе.
     * @param frameBufferName имя локального фреймбуффера пост эффекта.
     * @return сборщик пасса.
     *
     * @see LocalFrameBufferInfo
     */
    public ProgramPassBuilder input(int samplerId, String frameBufferName) {
        this.inputs.add(new Pair<>(samplerId, context -> context.framebuffers().get(frameBufferName)));
        return this;
    }

    /**
     * Добавляет входной фреймбуффер в пасс.
     *
     * @param samplerId айди семплера в программе.
     * @param framebuffer фреймбуффер.
     * @return сборщик пасса.
     *
     * @see Framebuffer
     */
    public ProgramPassBuilder input(int samplerId, Framebuffer framebuffer) {
        this.inputs.add(new Pair<>(samplerId, context -> framebuffer));
        return this;
    }

    /**
     * Устанавливает выходной фреймбуффер пасса.
     *
     * @param frameBufferName имя локального буффера пост эффекта.
     * @return сборщик пасса.
     *
     * @see LocalFrameBufferInfo
     */
    public ProgramPassBuilder output(String frameBufferName) {
        this.output = context -> context.framebuffers().get(frameBufferName);
        return this;
    }

    /**
     * Устанавливает выходной фреймбуффер пасса.
     *
     * @param framebuffer фреймбуффер.
     * @return сборщик пасса.
     *
     * @see Framebuffer
     */
    public ProgramPassBuilder output(Framebuffer framebuffer) {
        this.output = context -> framebuffer;
        return this;
    }

    /**
     * Устанавливает программу пасса.
     *
     * @param program программа, которая будет использована пассом при отрисовке.
     * @return сборщик пасса.
     *
     * @see GlProgram
     */
    public ProgramPassBuilder program(GlProgram program) {
        this.program = program;
        return this;
    }

    /**
     * Устанавливает действие, выполняемое перед выполнением программы.
     *
     * @param preRenderAction действие, выполняемое перед выполнением программы.
     * @return сборщик пасса.
     */
    public ProgramPassBuilder preRenderAction(Consumer<GlProgram> preRenderAction) {
        if (preRenderAction != null)
            this.preRenderConsumer = preRenderAction;
        return this;
    }

    /**
     * Собирает все данные в готовый пасс.
     *
     * @return новый пасс.
     *
     * @see ProgramPass
     */
    public ProgramPass build() {
        if (output == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing output in program pipeline."));
        if (program == null)
            ExceptionPrinter.printAndExit(new IllegalProgramPassBuilderArgumentException("Missing program in program pipeline."));

        return new ProgramPass(program, inputs, output, preRenderConsumer);
    }
}
