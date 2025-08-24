package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippetBuilder;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Matrix4fGlUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Vec4GlUniform;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
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
import org.joml.Vector4f;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;

import java.util.function.Function;

public class CometRenderer {
    private static boolean initialized = false;
    //Геттер анди буффера
    private static Function<GlGpuBuffer, Integer> bufferIdGetter;
    //Шейдерный цвет, который биндится шейдеру, если он ему нужен
    private static Vector4f shaderColor = new Vector4f(1f, 1f, 1f, 1f);
    //Сниппет для шейдеров, использующих матрицы для модификации координат вершин
    private static final GlProgramSnippet matrixSnippet = GlProgramSnippetBuilder.builder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX)
            .build();
    //Сниппет для шейдеров, использующих глобальный цвет шейдера
    private static final GlProgramSnippet colorSnippet = GlProgramSnippetBuilder.builder()
            .uniform("color", UniformType.VEC4)
            .build();
    private static GlProgram globalProgram;

    /*
     * Инициализация комет рендерера
     */
    public static void init(Function<GlGpuBuffer, Integer> bufferIdGetter) {
        //Не ну ты долбаеб если второй раз будешь инициализировать рендерер
        if (initialized)
            throw new IllegalStateException("CometRenderer has already initialized");


        //Устанавливаем геттер айди буффера
        CometRenderer.bufferIdGetter = bufferIdGetter;

        initialized = true;
    }

    /*
     * Возвращает геттер айди буффера
     */
    public static Function<GlGpuBuffer, Integer> getBufferIdGetter() {
        return bufferIdGetter;
    }

    /*
     * Возвращает текущий цвет шейдера
     */
    public static Vector4f getShaderColor() {
        return shaderColor;
    }

    /*
     * Устанавливает текущий цвет шейдера
     */
    public static void setShaderColor(Vector4f shaderColor) {
        CometRenderer.shaderColor = shaderColor;
    }

    /*
     * Устанавливает цвет шейдера по умолчанию
     */
    public static void resetColor() {
        setShaderColor(new Vector4f(1f, 1f, 1f, 1f));
    }

    /*
     * Возвращает сниппет для матриц
     */
    public static GlProgramSnippet getMatrixSnippet() {
        return matrixSnippet;
    }

    /*
     * Возвращает сниппет для глобального цвета шейдера
     */
    public static GlProgramSnippet getColorSnippet() {
        return colorSnippet;
    }

    /*
     * Устанавлиает юниформы матриц, если шейдер использовал соответствующий сниппет
     */
    public static void initMatrix() {
        BufferUniform projectionUniform = globalProgram.getUniform("Projection", BufferUniform.class);
        if (projectionUniform != null) {
            GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
            projectionUniform.set(slice);
        }

        Matrix4fGlUniform modelViewUniform = globalProgram.getUniform("modelViewMat", Matrix4fGlUniform.class);
        if (modelViewUniform != null)
            modelViewUniform.set(RenderSystem.getModelViewMatrix());
    }

    /*
     * Устанавливает униформу шейдерного цвета, если шейдер использовал соответствующий сниппет
     */
    public static void initShaderColor() {
        Vec4GlUniform colorUniform = globalProgram.getUniform("color", Vec4GlUniform.class);
        if (colorUniform != null)
            colorUniform.set(getShaderColor());
    }

    /*
     * Устанавливает текущую программу
     */
    public static void setGlobalProgram(GlProgram globalProgram) {
        CometRenderer.globalProgram = globalProgram;
    }

    /*
     * Возвращает текущую программу
     */
    public static GlProgram getGlobalProgram() {
        return globalProgram;
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

    /*
     * Рисует буффер и автоматически закрывает его
     */
    public static void drawBuffer(BuiltBuffer builtBuffer) {
        drawBuffer(builtBuffer, true);
    }

    /*
     * Рисует буффер и по выбору закрывает его
     */
    public static void drawBuffer(BuiltBuffer builtBuffer, boolean close) {
        globalProgram.bind();

        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();

        if (drawParameters.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawParameters.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex buffer", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            drawIndexed(0, 0, drawParameters.indexCount(), 1, drawParameters, indexType, vertexBuffer, indexBuffer);

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();

        globalProgram.unBind();
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
