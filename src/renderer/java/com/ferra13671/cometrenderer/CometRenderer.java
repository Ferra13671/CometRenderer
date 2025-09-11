package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.blend.DstFactor;
import com.ferra13671.cometrenderer.blend.SrcFactor;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.builder.snippet.GlProgramSnippetBuilder;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Matrix4fGlUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Vec4GlUniform;
import com.ferra13671.cometrenderer.scissor.ScissorStack;
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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class CometRenderer {
    private static boolean initialized = false;
    private static final Logger logger = LoggerFactory.getLogger("CometRenderer");
    //Геттер анди буффера
    private static Function<GlGpuBuffer, Integer> bufferIdGetter;
    //Геттер для 2д скейла
    private static Supplier<Integer> scaleGetter;
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
    //Глобальная программа рендерера, используемая при рендере
    private static GlProgram globalProgram;
    //Стек областей ножниц
    private static final ScissorStack scissorStack = new ScissorStack();

    /*
     * Инициализация комет рендерера
     */
    public static void init(Function<GlGpuBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        //Не ну ты долбаеб если второй раз будешь инициализировать рендерер
        if (initialized)
            throw new IllegalStateException("CometRenderer has already initialized");


        //Устанавливаем геттер айди буффера
        CometRenderer.bufferIdGetter = bufferIdGetter;
        //Устанавлиаем геттер 2д скейла
        CometRenderer.scaleGetter = scaleGetter;

        initialized = true;
    }

    public static Logger getLogger() {
        return logger;
    }

    /*
     * Возвращает геттер айди буффера
     */
    public static Function<GlGpuBuffer, Integer> getBufferIdGetter() {
        return bufferIdGetter;
    }

    /*
     * Возвращает геттер 2д скейла
     */
    public static Supplier<Integer> getScaleGetter() {
        return scaleGetter;
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
     * Возвращает стек области ножниц
     */
    public static ScissorStack getScissorStack() {
        return scissorStack;
    }

    /*
     * Включает смешивание цветов, если оно не включено, и устанавливает ему основную функцию
     */
    public static void applyDefaultBlend() {
        GlStateManager._enableBlend();
        GL11.glBlendFunc(SrcFactor.SRC_ALPHA.value, DstFactor.ONE_MINUS_SRC_ALPHA.value);
    }

    /*
     * Включает смешивание цветов, если оно не включено, и устанавливает ему кастомную функцию
     */
    public static void applyBlend(SrcFactor srcFactor, DstFactor dstFactor) {
        GlStateManager._enableBlend();
        GL11.glBlendFunc(srcFactor.value, dstFactor.value);
    }

    /*
     * Включает смешивание цветов, если оно не включено, и устанавливает ему кастомную функцию
     */
    public static void applyBlend(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        GlStateManager._enableBlend();
        GL14.glBlendFuncSeparate(srcColor.value, dstColor.value, srcAlpha.value, dstAlpha.value);
    }

    /*
     * Отключает смешивание цветов
     */
    public static void disableBlend() {
        GlStateManager._disableBlend();
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
        if (scissorStack.current() != null) {
            GlStateManager._enableScissorTest();
            scissorStack.current().bind();
        } else
            GlStateManager._disableScissorTest();

        globalProgram.bind();

        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();

        if (drawParameters.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawParameters.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex buffer", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            drawIndexed(drawParameters.indexCount(), drawParameters, indexType, vertexBuffer, indexBuffer);

            vertexBuffer.close();
        }
        if (close)
            builtBuffer.close();

        globalProgram.unBind();
    }

    private static void drawIndexed(int count, BuiltBuffer.DrawParameters drawParameters, VertexFormat.IndexType indexType, GpuBuffer vertexBuffer, GpuBuffer indexBuffer) {
        ((GlBackend) RenderSystem.getDevice()).getVertexBufferManager().setupBuffer(drawParameters.format(), (GlGpuBuffer) vertexBuffer);
        if (indexType != null) {
            GlStateManager._glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, bufferIdGetter.apply((GlGpuBuffer) indexBuffer));

            GlStateManager._drawElements(GlConst.toGl(drawParameters.mode()), count, GlConst.toGl(indexType), 0);
        } else {
            GlStateManager._drawArrays(GlConst.toGl(drawParameters.mode()), 0, count);
        }
    }
}
