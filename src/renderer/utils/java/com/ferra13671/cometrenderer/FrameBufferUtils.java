package com.ferra13671.cometrenderer;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;

/**
 * Класс с различными утилитами для фреймбуффера.
 */
public final class FrameBufferUtils {

    /**
     * Устанавливает активный фреймбуффер.
     *
     * @param id айди фреймбуффера.
     * @param colorTexture текстуры цвета фреймбуффера.
     */
    public static void bindFrameBuffer(int id, GpuTextureView colorTexture) {
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, id);

        GlStateManager._viewport(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0));
    }

    /**
     * Возвращает айди фреймбуффера.
     *
     * @param colorTexture текстура цвета фреймбуффера.
     * @param depthTexture текстура глубины фреймбуффера.
     * @return айди фреймбуффера.
     */
    public static int getFrameBufferId(GpuTextureView colorTexture, GpuTextureView depthTexture) {
        validateFrameBufferTexture("Color", colorTexture);

        if (depthTexture != null)
            validateFrameBufferTexture("Depth", depthTexture);

        return ((GlTexture)colorTexture.texture())
                .getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), depthTexture == null ? null : depthTexture.texture());
    }

    /**
     * Проверяет текстуру на валидность, если проверка не прошла то возвращается исключение.
     *
     * @param name имя текстуры.
     * @param gpuTextureView текстура.
     */
    public static void validateFrameBufferTexture(String name, GpuTextureView gpuTextureView) {
        if (gpuTextureView.isClosed())
            throw new IllegalStateException(name.concat("texture is closed"));

        if ((gpuTextureView.texture().usage() & 8) == 0)
            throw new IllegalStateException(name.concat("texture must have USAGE_RENDER_ATTACHMENT"));

        if (gpuTextureView.texture().getDepthOrLayers() > 1)
            throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
    }
}
