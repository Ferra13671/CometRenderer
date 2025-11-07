package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.builders.ProgramPassBuilder;
import com.ferra13671.cometrenderer.program.GlProgram;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Объект, представляющий собой этап отрисовки пост эффекта.
 *
 * @see PostEffectPipeline
 * @see ProgramPassBuilder
 */
public class ProgramPass {
    /** Программа, которая будет использоваться пассом. **/
    private final GlProgram program;
    /** Список входных фреймбуфферов, привязанных к семплерам программы. **/
    private final List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs;
    /** Выходной фреймбуффер пасса. **/
    private final Function<PostEffectContext, Framebuffer> output;
    /** Действие, выполняемое перед выполнением программы. **/
    private final Consumer<GlProgram> preRenderConsumer;

    /**
     * @param program программа, которая будет использоваться пассом.
     * @param inputs список входных фреймбуфферов, привязанных к семплерам программы.
     * @param output выходной фреймбуффер пасса.
     * @param preRenderConsumer действие, выполняемое перед выполнением программы.
     */
    public ProgramPass(GlProgram program, List<Pair<Integer, Function<PostEffectContext, Framebuffer>>> inputs, Function<PostEffectContext, Framebuffer> output, Consumer<GlProgram> preRenderConsumer) {
        this.program = program;
        this.inputs = inputs;
        this.output = output;
        this.preRenderConsumer = preRenderConsumer;
    }

    /**
     * Выполняет отрисовку пасса с использованием контекста пост эффекта.
     *
     * @param context контекст пост эффекта.
     *
     * @see PostEffectContext
     */
    public void execute(PostEffectContext context) {
        //Биндим выходной фреймбуффер
        Framebuffer outputFrameBuffer = output.apply(context);
        CometRenderer.bindFramebuffer(outputFrameBuffer.getColorAttachmentView(), outputFrameBuffer.getDepthAttachmentView());

        //Биндим программу
        CometRenderer.setGlobalProgram(program);
        //Инициализируем матрицы
        CometRenderer.initMatrix();
        //Биндим все входные фреймбуфферы
        for (Pair<Integer, Function<PostEffectContext, Framebuffer>> input : inputs)
            program.getSampler(input.getLeft()).set((GlTextureView) input.getRight().apply(context).getColorAttachmentView());
        //Выполняем действие перед рендером
        preRenderConsumer.accept(program);

        //Рендерим
        CometRenderer.draw(context.mesh(), false);
    }

    /**
     * Возвращает новый сборщик пасса.
     *
     * @return новый сборщик пасса.
     *
     * @see ProgramPassBuilder
     */
    public static ProgramPassBuilder builder() {
        return new ProgramPassBuilder();
    }
}
