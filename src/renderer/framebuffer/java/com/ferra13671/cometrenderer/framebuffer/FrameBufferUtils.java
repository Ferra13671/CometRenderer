package com.ferra13671.cometrenderer.framebuffer;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;

public final class FrameBufferUtils {

    /*
     * Биндит фреймбуффер
     */
    public static void bindFrameBuffer(int id, GpuTextureView colorTexture) {
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, id);

        GlStateManager._viewport(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0));
    }

    /*
     * Возвращает айди фреймбуффера
     */
    public static int getFrameBufferId(GpuTextureView gpuTextureView, GpuTextureView gpuTextureView2) {
        validateFrameBufferTexture("Color", gpuTextureView);

        if (gpuTextureView2 != null)
            validateFrameBufferTexture("Depth", gpuTextureView2);

        return ((GlTexture)gpuTextureView.texture())
                .getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), gpuTextureView2 == null ? null : gpuTextureView2.texture());
    }

    /*
     * Проверяет GpuTextureView на валидность, если проверка не прошла то возвращается исключение
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
