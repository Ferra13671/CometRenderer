package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Matrix4fGlUniform;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.ferra13671.cometrenderer.test.mixins.ProjectionMatrix2Accessor;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.ProjectionMatrix2;
import net.minecraft.client.texture.GlTexture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

public class RenderUtils implements Mc {
    private static final ProjectionMatrix2 matrix = new ProjectionMatrix2("bthack-projection-matrix", -1000, 1000, true);
    public static Matrix4f projectionMatrix = new Matrix4f();

    public static void setMatrixUniforms(GlProgram program) {
        BufferUniform projectionUniform = program.getUniform("Projection", BufferUniform.class);
        if (projectionUniform != null) {
            GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
            projectionUniform.set(new BufferUniform.BufferData(((IGlGpuBuffer) slice.buffer())._getId(), slice.offset(), slice.length()));
        }

        Matrix4fGlUniform modelViewUniform = program.getUniform("modelViewMat", Matrix4fGlUniform.class);
        if (modelViewUniform != null)
            modelViewUniform.set(RenderSystem.getModelViewMatrix());
    }

    /*
     * Убирает майнкрафтовский масштаб гуи и устанавливает кастомный
     */
    public static void unscaledProjection() {
        float width = mc.getWindow().getFramebufferWidth() / (float) 2;
        float height = mc.getWindow().getFramebufferHeight() / (float) 2;

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.ORTHOGRAPHIC);
        projectionMatrix.set(((ProjectionMatrix2Accessor) matrix)._getMatrix(width, height));
    }

    /*
     * Возвращает масштаб обратно
     */
    public static void scaledProjection() {
        float width = (float) (mc.getWindow().getFramebufferWidth() / 2f);
        float height = (float) (mc.getWindow().getFramebufferHeight() / 2f);

        RenderSystem.setProjectionMatrix(matrix.set(width, height), ProjectionType.PERSPECTIVE);
        projectionMatrix.set(((ProjectionMatrix2Accessor) matrix)._getMatrix(width, height));
    }

    public static void bindDefaultFrameBufferAttachment() {
        Framebuffer framebuffer = mc.getFramebuffer();

        if (framebuffer != null) {
            if (framebuffer.getColorAttachmentView() != null && framebuffer.getDepthAttachmentView() != null)
                attachment(framebuffer.getColorAttachmentView(), framebuffer.getDepthAttachmentView());
        }
    }

    public static void attachment(GpuTextureView gpuTextureView, GpuTextureView gpuTextureView2) {
        if (gpuTextureView.isClosed()) {
            throw new IllegalStateException("Color texture is closed");
        } else if ((gpuTextureView.texture().usage() & 8) == 0) {
            throw new IllegalStateException("Color texture must have USAGE_RENDER_ATTACHMENT");
        } else if (gpuTextureView.texture().getDepthOrLayers() > 1) {
            throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
        } else {
            if (gpuTextureView2 != null) {
                if (gpuTextureView2.isClosed()) {
                    throw new IllegalStateException("Depth texture is closed");
                }

                if ((gpuTextureView2.texture().usage() & 8) == 0) {
                    throw new IllegalStateException("Depth texture must have USAGE_RENDER_ATTACHMENT");
                }

                if (gpuTextureView2.texture().getDepthOrLayers() > 1) {
                    throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
                }
            }

            int i = ((GlTexture)gpuTextureView.texture())
                    .getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), gpuTextureView2 == null ? null : gpuTextureView2.texture());
            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, i);

            GlStateManager._viewport(0, 0, gpuTextureView.getWidth(0), gpuTextureView.getHeight(0));
        }
    }

    /*
     * Рисует буффер
     */
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
            GlStateManager._glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, ((IGlGpuBuffer) indexBuffer)._getId());
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
