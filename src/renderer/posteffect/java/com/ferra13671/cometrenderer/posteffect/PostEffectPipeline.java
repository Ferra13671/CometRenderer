package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.builders.PostEffectPipelineBuilder;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.CometVertexFormats;
import net.minecraft.client.gl.Framebuffer;

import java.util.HashMap;
import java.util.List;

/**
 * Пост эффект, позволяющий накладывать "эффекты" на фреймбуффер.
 * При выполнении пост эффект так же может создавать свои локальные фреймбуфферы, а так же может состоять из нескольких пассов.
 *
 * @see ProgramPass
 * @see LocalFrameBufferInfo
 * @see PostEffectPipelineBuilder
 */
public class PostEffectPipeline {
    /** Список информации о локальных фреймбуфферах. **/
    private final List<LocalFrameBufferInfo> localFrameBuffers;
    /** Список всех пассов пост эффекта. **/
    private final List<ProgramPass> passes;

    /**
     * @param localFrameBuffers список информации о локальных фреймбуфферах.
     * @param passes список всех пассов пост эффекта.
     */
    public PostEffectPipeline(List<LocalFrameBufferInfo> localFrameBuffers, List<ProgramPass> passes) {
        this.localFrameBuffers = localFrameBuffers;
        this.passes = passes;
    }

    /**
     * Выполняет отрисовку пост эффекта.
     *
     * @param textureWidth длина текстуры.
     * @param textureHeight высота текстуры.
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
        PostEffectContext context = new PostEffectContext(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION, builder -> {
            builder.vertex(0f, textureHeight, 0f);
            builder.vertex(textureWidth, textureHeight, 0f);
            builder.vertex(textureWidth, 0, 0f);
            builder.vertex(0f, 0f, 0f);
        }), frameBuffers);

        //Выполняем все действия пост эффекта
        for (ProgramPass pass : passes)
            pass.execute(context);

        //Закрываем буффер вершин и все локальные фреймбуфферы
        context.mesh().close();
        for (Framebuffer f : context.framebuffers().values())
            f.delete();
    }

    /**
     * Возвращает новый сборщик пост эффекта.
     *
     * @return новый сборщик пост эффекта.
     */
    public static PostEffectPipelineBuilder builder() {
        return new PostEffectPipelineBuilder();
    }
}
