package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.blend.DstFactor;
import com.ferra13671.cometrenderer.blend.SrcFactor;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.buffer.BufferUniform;
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

/**
 * Главный класс рендеринга.
 * Контролирует всю работу рендера.
 */
//TODO bindFramebuffer(FrameBuffer framebuffer)
//TODO переименовать bindMainFrameBuffer в bindMainFramebuffer
//TODO переименовать bindFrameBuffer в bindFramebuffer
public class CometRenderer {
    /** Состояние инициализации рендера. **/
    private static boolean initialized = false;
    /** Логгер CometRender'a, используемый для отправки ошибок. **/
    private static final Logger logger = LoggerFactory.getLogger("CometRenderer");
    /** Функция для получения айди GlGpuBuffer. **/
    private static Function<GlGpuBuffer, Integer> bufferIdGetter;
    /** Метод, возвращающий скейл рендеринга. **/
    private static Supplier<Integer> scaleGetter;
    /** Глобальный шейдерный цвет, позволяющий контролировать цвет выходных объектов рендеринга, если программа реализовала данную возможность. **/
    private static Vector4f shaderColor = new Vector4f(1f, 1f, 1f, 1f);
    /** Фрагмент программы, необходимый для вершинных шейдеров, использующих основные матрицы для модификации координат вершин. **/
    private static final GlProgramSnippet matrixSnippet = GlProgramSnippet.builder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX)
            .build();
    /** Фрагмент программы, необходимый для программ, которые хотят реализовать использование глобального шейдерного цвета. **/
    //TODO переименовать название униформы в shaderColor
    private static final GlProgramSnippet colorSnippet = GlProgramSnippet.builder()
            .uniform("color", UniformType.VEC4)
            .build();
    /** Глобальная активная программа для CometRenderer'а, которая будет использоваться для отрисовки. **/
    private static GlProgram globalProgram;
    /** Стек для областей, используемых ножницами. **/
    private static final ScissorStack scissorStack = new ScissorStack();

    /**
     * Инициализирует CometRenderer.
     *
     * @param bufferIdGetter функция для получения айди GlGpuBuffer.
     * @param scaleGetter метод, возвращающий скейл рендеринга.
     */
    public static void init(Function<GlGpuBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        //Не ну ты долбаеб если второй раз будешь инициализировать рендерер
        if (initialized)
            throw new IllegalStateException("CometRenderer has already initialized.");


        //Устанавливаем геттер айди буффера
        CometRenderer.bufferIdGetter = bufferIdGetter;
        //Устанавлиаем геттер 2д скейла
        CometRenderer.scaleGetter = scaleGetter;

        initialized = true;
    }

    /**
     * Возвращает логгер CometRenderer'а.
     *
     * @return логгер CometRenderer'а.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Возвращает функцию для получения айди GlGpuBuffer.
     *
     * @return функция для получения айди GlGpuBuffer.
     */
    public static Function<GlGpuBuffer, Integer> getBufferIdGetter() {
        return bufferIdGetter;
    }

    /**
     * Возвращает метод, возвращающий скейл рендеринга.
     *
     * @return метод, возвращающий скейл рендеринга.
     */
    public static Supplier<Integer> getScaleGetter() {
        return scaleGetter;
    }

    /**
     * Возвращает глобальный шейдерный цвет, позволяющий контролировать цвет выходных объектов рендеринга, если программа реализовала данную возможность.
     *
     * @return глобальный шейдерный цвет.
     */
    public static Vector4f getShaderColor() {
        return shaderColor;
    }

    /**
     * Устанавливает текущий глобальный шейдерный цвет.
     *
     * @param shaderColor глобальный шейдерный цвет.
     */
    public static void setShaderColor(Vector4f shaderColor) {
        CometRenderer.shaderColor = shaderColor;
    }

    /**
     * Устанавливает глобальный шейдерный цвет по умолчанию.
     */
    public static void resetShaderColor() {
        setShaderColor(new Vector4f(1f, 1f, 1f, 1f));
    }

    /**
     * Возвращает фрагмент программы, необходимый для вершинных шейдеров, использующих основные матрицы для модификации координат вершин.
     *
     * @return фрагмент программы, добавляющий униформы основных матриц.
     *
     * @see CometRenderer#matrixSnippet
     */
    public static GlProgramSnippet getMatrixSnippet() {
        return matrixSnippet;
    }

    /**
     * Возвращает фрагмент программы, необходимый для программ, которые хотят реализовать использование глобального шейдерного цвета.
     *
     * @return фрагмент программы, добавляющий униформу глобального шейдерного цвета.
     *
     * @see CometRenderer#colorSnippet
     */
    public static GlProgramSnippet getColorSnippet() {
        return colorSnippet;
    }

    /**
     * Устанавливает основные матрицы в униформы, если программа использует фрагмент программы для основных матриц.
     *
     * @see CometRenderer#matrixSnippet
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

    /**
     * Устанавливает глобальный шейдерный цвет в униформу, если программа использует фрагмент программы для глобального шейдерного цвета.
     *
     * @see CometRenderer#colorSnippet
     */
    public static void initShaderColor() {
        Vec4GlUniform colorUniform = globalProgram.getUniform("color", UniformType.VEC4);
        if (colorUniform != null)
            colorUniform.set(getShaderColor());
    }

    /**
     * Устанавливает данную программу как активную для CometRenderer'а.
     *
     * @param globalProgram программа, которую нужно сделать активной.
     */
    public static void setGlobalProgram(GlProgram globalProgram) {
        CometRenderer.globalProgram = globalProgram;
    }

    /**
     * Возвращает текущую активную программу для CometRenderer'а.
     *
     * @return текущая активная программа для CometRenderer'а.
     */
    public static GlProgram getGlobalProgram() {
        return globalProgram;
    }

    /**
     * Возвращает стек для областей, используемых ножницами.
     *
     * @return стек для областей, используемых ножницами.
     */
    public static ScissorStack getScissorStack() {
        return scissorStack;
    }

    /**
     * Включает смешивание, если оно выключено, и устанавливает ему основные множители уравнения.
     *
     * @see <a href="https://docs.gl/gl4/glBlendFunc">OpenGL glBlendFunc wiki</a>
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void applyDefaultBlend() {
        GlStateManager._enableBlend();
        GL11.glBlendFunc(SrcFactor.SRC_ALPHA.glId, DstFactor.ONE_MINUS_SRC_ALPHA.glId);
    }

    /**
     * Включает смешивание, если оно выключено, и устанавливает ему данные множители уравнения.
     *
     * @param srcFactor множитель для нового цвета в уравнении смешивания.
     * @param dstFactor множитель для уже имеющегося цвета в фреймбуффере в уравнении смешивания.
     *
     * @see <a href="https://docs.gl/gl4/glBlendFunc">OpenGL glBlendFunc wiki</a>
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void applyBlend(SrcFactor srcFactor, DstFactor dstFactor) {
        GlStateManager._enableBlend();
        GL11.glBlendFunc(srcFactor.glId, dstFactor.glId);
    }

    /**
     * Включает смешивание, если оно выключено, и устанавливаем ему данные множители уравнения.
     *
     * @param srcColor множитель для нового цвета в уравнении смешивания (для RGB).
     * @param dstColor множитель для уже имеющегося цвета в фреймбуффере в уравнении смешивания (для RGB).
     * @param srcAlpha множитель для нового цвета в уравнении смешивания (для прозрачности).
     * @param dstAlpha множитель для уже имеющегося цвета в фреймбуффере в уравнении смешивания (для прозрачности).
     *
     * @see <a href="https://docs.gl/gl4/glBlendFuncSeparate">OpenGL glBlendFuncSeparate wiki</a>
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void applyBlend(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        GlStateManager._enableBlend();
        GL14.glBlendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);
    }

    /**
     * Отключает смешивание, если оно включено.
     *
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void disableBlend() {
        GlStateManager._disableBlend();
    }

    /**
     * Устанавливает основной фреймбуффер майнкрафта как активный.
     *
     * @see Framebuffer
     */
    public static void bindMainFrameBuffer() {
        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

        if (framebuffer != null) {
            if (framebuffer.getColorAttachmentView() != null && framebuffer.getDepthAttachmentView() != null)
                bindFrameBuffer(framebuffer.getColorAttachmentView(), framebuffer.getDepthAttachmentView());
        }
    }

    /**
     * Устанавливает данный фреймбуффер как активный.
     *
     * @param colorTexture текстура цвета фреймбуффера.
     * @param depthTexture текстура глубины фреймбуффера.
     *
     * @see Framebuffer
     * @see GpuTextureView
     */
    public static void bindFrameBuffer(GpuTextureView colorTexture, GpuTextureView depthTexture) {
        FrameBufferUtils.bindFrameBuffer(
                FrameBufferUtils.getFrameBufferId(colorTexture, depthTexture),
                colorTexture
        );
    }

    /**
     * Создаёт готовый меш с вершинами. Перед сборкой меша вызывается данный вами метод, что бы добавить в сборщика данные о вершинах.
     * Метод ввернет null, если в сборщике нет вершин.
     *
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершины.
     * @param buildConsumer метод для добавления в сборщика данных о вершинах.
     * @return готовый меш либо null, если в сборщике нет вершин.
     */
    public static Mesh createMesh(DrawMode drawMode, VertexFormat vertexFormat, Consumer<MeshBuilder> buildConsumer) {
        MeshBuilder meshBuilder = Mesh.builder(drawMode, vertexFormat);
        buildConsumer.accept(meshBuilder);
        return meshBuilder.buildNullable();
    }

    /**
     * Вызывает цикл отрисовки для реализации буффера вершин от майнкрафта и автоматически закрывает его.
     *
     * @param builtBuffer реализация буффера вершин от майнкрафта.
     *
     * @see BuiltBuffer
     */
    public static void draw(BuiltBuffer builtBuffer) {
        draw(BufferRenderers.MINECRAFT_BUFFER, builtBuffer, true);
    }

    /**
     * Вызывает цикл отрисовки для реализации буффера вершин от майнкрафта и закрывает его по вашему желанию.
     *
     * @param builtBuffer реализация буффера вершин от майнкрафта.
     * @param close закрывать после отрисовки буффер вершин или нет.
     */
    public static void draw(BuiltBuffer builtBuffer, boolean close) {
        draw(BufferRenderers.MINECRAFT_BUFFER, builtBuffer, close);
    }

    /**
     * Вызывает цикл отрисовки для меша и автоматически закрывает его.
     *
     * @param mesh меш.
     *
     * @see IMesh
     */
    public static void draw(IMesh mesh) {
        draw(BufferRenderers.COMET_BUFFER, mesh, true);
    }

    /**
     * Вызывает цикл отрисовки для меша и закрывает его по вашему желанию.
     *
     * @param mesh меш.
     * @param close закрывать после отрисовки буффер вершин или нет.
     *
     * @see IMesh
     */
    public static void draw(IMesh mesh, boolean close) {
        draw(BufferRenderers.COMET_BUFFER, mesh, close);
    }

    /**
     * Вызывает цикл отрисовки при помощи данного отрисовщика и буффера вершин.
     * Так же закрывает данный буффер вершин по вашему желанию.
     *
     * @param renderConsumer отрисовщик буффера вершин.
     * @param builtBuffer буффер вершин.
     * @param close закрывать после отрисовки буффер вершин или нет.
     * @param <T> тип буффера вершин.
     */
    public static <T> void draw(BiConsumer<T, Boolean> renderConsumer, T builtBuffer, boolean close) {
        if (scissorStack.current() != null) {
            GlStateManager._enableScissorTest();
            scissorStack.current().bind();
        } else
            GlStateManager._disableScissorTest();

        globalProgram.bind();

        renderConsumer.accept(builtBuffer, close);

        globalProgram.unbind();
    }
}
