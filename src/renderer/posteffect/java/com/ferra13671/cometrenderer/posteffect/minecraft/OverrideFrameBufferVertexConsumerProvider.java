package com.ferra13671.cometrenderer.posteffect.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

/*
 * Модификация обычного VertexConsumerProvider.Immediate, необходимая для дополнительной отрисовки всего контента в другой фреймбуффер.
 * Необходимо для пост эффектных шейдеров.
 */
public class OverrideFrameBufferVertexConsumerProvider extends VertexConsumerProvider.Immediate {
    //Родительный VertexConsumerProvider.Immediate (с которого мы будем "пиздить" рендер)
    private final Immediate parent;
    //Фреймбуффер, в который будем перенаправлять рендер
    private final CometFrameBuffer overrideFrameBuffer;
    //Изначальный фреймбуффер
    private final Framebuffer outputFrameBuffer;
    //Очищать глубину или нет
    private final boolean clearDepth, drawBack;

    /*
     * parent — родительный VertexConsumerProvider.Immediate (с которого мы будем "пиздить" рендер)
     * overrideFrameBuffer — фреймбуффер, в который будет дополнительно отрисован весь контент
     * outputFrameBuffer — изначальный фреймбуффер
     * clearDepth — очищать глубину фреймбуффера или нет (true для рук)
     * drawBack — отрисовывать ли обратно в фреймбуффер то, что мы спиздили, или нет
     */
    public OverrideFrameBufferVertexConsumerProvider(Immediate parent, CometFrameBuffer overrideFrameBuffer, Framebuffer outputFrameBuffer, boolean clearDepth, boolean drawBack) {
        super(null, null);
        this.parent = parent;
        this.overrideFrameBuffer = overrideFrameBuffer;
        this.outputFrameBuffer = outputFrameBuffer;
        this.clearDepth = clearDepth;
        this.drawBack = drawBack;
    }

    @Override
    @OverriddenMethod
    public VertexConsumer getBuffer(RenderLayer renderLayer) {
        return this.parent.getBuffer(renderLayer);
    }

    @Override
    @OverriddenMethod
    public void draw() {
        //Очищает текстуру цвета
        overrideFrameBuffer.clearColorTexture();

        //Перенаправляем рендер
        RenderSystem.outputColorTextureOverride = overrideFrameBuffer.getColorAttachmentView();
        RenderSystem.outputDepthTextureOverride = overrideFrameBuffer.getDepthAttachmentView();
        this.parent.draw();

        //Возвращаем всё обратно
        RenderSystem.outputColorTextureOverride = null;
        RenderSystem.outputDepthTextureOverride = null;

        //Очищаем глубину при необходимости
        if (clearDepth)
            overrideFrameBuffer.clearDepthTexture();

        //Отрисовываем то, что получили, в изначальный фреймбуффер, если это нужно
        if (this.drawBack)
            overrideFrameBuffer.drawBlit(outputFrameBuffer.getColorAttachmentView());
    }
}
