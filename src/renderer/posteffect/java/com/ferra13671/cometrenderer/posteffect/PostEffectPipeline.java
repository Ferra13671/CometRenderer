package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.CometVertexFormats;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;

import java.util.HashMap;
import java.util.List;

/*
 * Пост эффект, позволяющий накладывать "эффекты" на фреймбуффер.
 * При выполнении так же может создавать свои локальные фреймбуфферы, а так же может состоять из нескольких "подэффектов".
 */
public class PostEffectPipeline {
    //Список информации о локальных фреймбуфферах
    private final List<LocalFrameBufferInfo> localFrameBuffers;
    //Список всех действий пост эффекта
    private final List<PostEffectPipelineAction> actions;

    public PostEffectPipeline(List<LocalFrameBufferInfo> localFrameBuffers, List<PostEffectPipelineAction> actions) {
        this.localFrameBuffers = localFrameBuffers;
        this.actions = actions;
    }

    /*
     * Выполняет отрисовку пост эффекта
     */
    public void execute(int textureWidth, int textureHeight) {
        //Создаём все локальные фреймбуфферы
        HashMap<String, CometFrameBuffer> frameBuffers = new HashMap<>();
        for (LocalFrameBufferInfo info : localFrameBuffers) {
            CometFrameBuffer f = new CometFrameBuffer(info.name(), textureWidth, textureHeight, info.clearColor(), false);
            f.clearColorTexture();
            frameBuffers.put(info.name(), f);
        }

        //Создаём контекст
        PostEffectContext context = new PostEffectContext(CometRenderer.createBuiltBuffer(DrawMode.QUADS, CometVertexFormats.POSITION, builder -> {
            builder.vertex(0f, textureHeight, 0f);
            builder.vertex(textureWidth, textureHeight, 0f);
            builder.vertex(textureWidth, 0, 0f);
            builder.vertex(0f, 0f, 0f);
        }), frameBuffers);

        //Выполняем все действия пост эффекта
        for (PostEffectPipelineAction action : actions)
            action.execute(context);

        //Закрываем буффер вершин и все локальные фреймбуфферы
        context.buffer().close();
        for (Framebuffer f : context.framebuffers().values())
            f.delete();
    }

    /*
     * Возвращает новый билдер пост эффекта
     */
    public static PostEffectPipelineBuilder builder() {
        return new PostEffectPipelineBuilder();
    }
}
