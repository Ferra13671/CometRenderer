package com.ferra13671.cometrenderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.texture.GlTexture;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

import java.util.function.Function;

public class CometRenderer {
    private static boolean initialized = false;
    private static Function<GlGpuBuffer, Integer> bufferIdGetter;

    public static void init(Function<GlGpuBuffer, Integer> bufferIdGetter) {
        if (initialized)
            throw new IllegalStateException("CometRenderer has already initialized");

        CometRenderer.bufferIdGetter = bufferIdGetter;

        initialized = true;
    }

    public static Function<GlGpuBuffer, Integer> getBufferIdGetter() {
        return bufferIdGetter;
    }

    /*
     * Биндит основной фреймбуффер майнкрафта
     */
    public static void bindMainFrameBuffer() {
        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

        if (framebuffer != null) {
            if (framebuffer.getColorAttachmentView() != null && framebuffer.getDepthAttachmentView() != null)
                bindFrameBuffer(framebuffer.getColorAttachmentView(), framebuffer.getDepthAttachmentView());
        }
    }

    /*
     * Биндит фреймбуффер
     */
    public static void bindFrameBuffer(GpuTextureView gpuTextureView, GpuTextureView gpuTextureView2) {
        validateFrameBufferTexture("Color", gpuTextureView);

        if (gpuTextureView2 != null)
            validateFrameBufferTexture("Depth", gpuTextureView2);

        int i = ((GlTexture)gpuTextureView.texture())
                .getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), gpuTextureView2 == null ? null : gpuTextureView2.texture());
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, i);

        GlStateManager._viewport(0, 0, gpuTextureView.getWidth(0), gpuTextureView.getHeight(0));
    }

    /*
     * Проверяет GpuTextureView на валидность, если проверка не прошла то возвращается исключение
     */
    private static void validateFrameBufferTexture(String name, GpuTextureView gpuTextureView) {
        if (gpuTextureView.isClosed())
            throw new IllegalStateException(name.concat(" texture is closed"));

        if ((gpuTextureView.texture().usage() & 8) == 0)
            throw new IllegalStateException(name.concat(" texture must have USAGE_RENDER_ATTACHMENT"));

        if (gpuTextureView.texture().getDepthOrLayers() > 1)
            throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
    }

    public static void drawBuffer(BuiltBuffer builtBuffer) {
        drawBuffer(builtBuffer, true);
    }

    public static void drawBuffer(BuiltBuffer builtBuffer, boolean close) {
        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();

        if (drawParameters.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawParameters.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "BufferRenderer vertex buffer", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            drawIndexed(0, 0, drawParameters.indexCount(), 1, drawParameters, indexType, vertexBuffer, indexBuffer);

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();
    }

    private static void drawIndexed(int baseVertex, int firstIndex, int count, int instanceCount, BuiltBuffer.DrawParameters drawParameters, VertexFormat.IndexType indexType, GpuBuffer vertexBuffer, GpuBuffer indexBuffer) {
        ((GlBackend) RenderSystem.getDevice()).getVertexBufferManager().setupBuffer(drawParameters.format(), (GlGpuBuffer) vertexBuffer);
        if (indexType != null) {
            GlStateManager._glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, bufferIdGetter.apply((GlGpuBuffer) indexBuffer));
            if (instanceCount > 1) {
                if (baseVertex > 0) {
                    GL32.glDrawElementsInstancedBaseVertex(
                            GlConst.toGl(drawParameters.mode()), count, GlConst.toGl(indexType), (long)firstIndex * indexType.size, instanceCount, baseVertex
                    );
                } else {
                    GL31.glDrawElementsInstanced(
                            GlConst.toGl(drawParameters.mode()), count, GlConst.toGl(indexType), (long)firstIndex * indexType.size, instanceCount
                    );
                }
            } else if (baseVertex > 0) {
                GL32.glDrawElementsBaseVertex(
                        GlConst.toGl(drawParameters.mode()), count, GlConst.toGl(indexType), (long)firstIndex * indexType.size, baseVertex
                );
            } else {
                GlStateManager._drawElements(GlConst.toGl(drawParameters.mode()), count, GlConst.toGl(indexType), (long)firstIndex * indexType.size);
            }
        } else if (instanceCount > 1) {
            GL31.glDrawArraysInstanced(GlConst.toGl(drawParameters.mode()), baseVertex, count, instanceCount);
        } else {
            GlStateManager._drawArrays(GlConst.toGl(drawParameters.mode()), baseVertex, count);
        }
    }
}
