package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.program.GlProgram;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * "Под эффект" пост эффекта.
 * Можно настроить входные фреймбуфферы, выходной фреймбуффер и действие перед рендером
 */
public class ProgramPass implements PostEffectPipelineAction {
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

    /*
     * Выполняет отрисовку с использованием контекста пост эффекта
     */
    public void execute(PostEffectContext context) {
        //Биндим выходной фреймбуффер
        Framebuffer outputFrameBuffer = output.apply(context);
        CometRenderer.bindFrameBuffer(outputFrameBuffer.getColorAttachmentView(), outputFrameBuffer.getDepthAttachmentView());

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
        CometRenderer.draw(context.buffer(), false);
    }

    /*
     * Возвращает новый билдер "под эффекта" пост эффекта
     */
    public static ProgramPassBuilder builder() {
        return new ProgramPassBuilder();
    }
}
