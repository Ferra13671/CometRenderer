package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.blend.DstFactor;
import com.ferra13671.cometrenderer.blend.SrcFactor;
import com.ferra13671.cometrenderer.framebuffer.FrameBufferUtils;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Matrix4fGlUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Vec4GlUniform;
import com.ferra13671.cometrenderer.scissor.ScissorStack;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
    private static final GlProgramSnippet matrixSnippet = GlProgramSnippet.builder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX)
            .build();
    //Сниппет для шейдеров, использующих глобальный цвет шейдера
    private static final GlProgramSnippet colorSnippet = GlProgramSnippet.builder()
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
        BufferUniform projectionUniform = globalProgram.getUniform("Projection", UniformType.BUFFER);
        if (projectionUniform != null) {
            GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
            projectionUniform.set(slice);
        }

        Matrix4fGlUniform modelViewUniform = globalProgram.getUniform("modelViewMat", UniformType.MATRIX);
        if (modelViewUniform != null)
            modelViewUniform.set(RenderSystem.getModelViewMatrix());
    }

    /*
     * Устанавливает униформу шейдерного цвета, если шейдер использовал соответствующий сниппет
     */
    public static void initShaderColor() {
        Vec4GlUniform colorUniform = globalProgram.getUniform("color", UniformType.VEC4);
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
    public static void bindFrameBuffer(GpuTextureView colorTexture, GpuTextureView depthTexture) {
        FrameBufferUtils.bindFrameBuffer(
                FrameBufferUtils.getFrameBufferId(colorTexture, depthTexture),
                colorTexture
        );
    }

    /*
     * Создаёт готовый буффер с вершинами. Перед построением буффера вызывается buildConsumer, что бы записать в билдер данные о вершинах.
     * Если в итоге в билдер ничего не было записано, то вернётся null.
     */
    public static Mesh createMesh(DrawMode drawMode, VertexFormat vertexFormat, Consumer<MeshBuilder> buildConsumer) {
        MeshBuilder meshBuilder = Mesh.builder(drawMode, vertexFormat);
        buildConsumer.accept(meshBuilder);
        return meshBuilder.buildNullable();
    }

    /*
     * Рисует буффер и автоматически закрывает его
     */
    public static void draw(BuiltBuffer builtBuffer) {
        draw(BufferRenderers.MINECRAFT_BUFFER, builtBuffer, true);
    }

    /*
     * Рисует буффер и по выбору закрывает его
     */
    public static void draw(BuiltBuffer builtBuffer, boolean close) {
        draw(BufferRenderers.MINECRAFT_BUFFER, builtBuffer, close);
    }

    /*
     * Рисует буффер и автоматически закрывает его
     */
    public static void draw(IMesh mesh) {
        draw(BufferRenderers.COMET_BUFFER, mesh, true);
    }

    /*
     * Рисует буффер и по выбору закрывает его
     */
    public static void draw(IMesh mesh, boolean close) {
        draw(BufferRenderers.COMET_BUFFER, mesh, close);
    }

    private static <T> void draw(BiConsumer<T, Boolean> renderConsumer, T builtBuffer, boolean close) {
        if (scissorStack.current() != null) {
            GlStateManager._enableScissorTest();
            scissorStack.current().bind();
        } else
            GlStateManager._disableScissorTest();

        globalProgram.bind();

        renderConsumer.accept(builtBuffer, close);

        globalProgram.unBind();
    }
}
