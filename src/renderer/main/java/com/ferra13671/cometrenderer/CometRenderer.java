package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.sampler.ISamplerManger;
import com.ferra13671.cometrenderer.sampler.empty.EmptySamplerManager;
import com.ferra13671.cometrenderer.sampler.impl.SamplerManagerImpl;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.Logger;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.exceptions.impl.UnsupportedOpenGLVersionException;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.scissor.ScissorStack;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.IndexBufferGenerator;
import com.ferra13671.cometrenderer.vertex.format.uploader.VertexFormatManager;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;

import java.util.function.Consumer;

/**
 * Главный класс рендеринга.
 * Контролирует всю работу рендера.
 */
public class CometRenderer {
    /** Реестр различных данных. **/
    @Getter
    private static final Registry registry = new Registry();
    /** Конфиг с различными настройками. **/
    @Getter
    private static final Config config = new Config();
    /** Глобальный шейдерный цвет, позволяющий контролировать цвет выходных объектов рендеринга, если программа реализовала данную возможность. **/
    @Getter
    @Setter
    private static Vector4f shaderColor = new Vector4f(1f, 1f, 1f, 1f);
    /** Фрагмент программы, необходимый для программ, которые хотят реализовать использование глобального шейдерного цвета. **/
    @Getter
    private static final GlProgramSnippet colorSnippet = CometLoaders.IN_JAR.createProgramBuilder()
            .uniform("shaderColor", UniformType.VEC4)
            .buildSnippet();
    /** Глобальная активная программа для CometRenderer'а, которая будет использоваться для отрисовки. **/
    @Getter
    @Setter
    private static GlProgram globalProgram;
    /** Стек для областей, используемых ножницами. **/
    @Getter
    private static final ScissorStack scissorStack = new ScissorStack();
    @Getter
    private static ISamplerManger samplerManager;
    /** Логгер CometRender'a, используемый для отправки ошибок. **/
    @Getter
    @Setter
    private static Logger logger = new Logger() {
        @Override
        public void log(String message) {
            System.out.println(message);
        }

        @Override
        public void warn(String message) {
            System.err.println("[WARN] " + message);
        }

        @Override
        public void error(String message) {
            System.err.println(message);
        }
    };
    private static final BufferRenderer<IMesh> COMET_BUFFER_RENDERER = (mesh, close) -> {
        int vertexCount = mesh.getVertexCount();
        DrawMode drawMode = mesh.getDrawMode();

        if (vertexCount > 0) {
            VertexFormatManager.uploadFormatToBuffer(mesh.getVertexBuffer(), mesh.getVertexFormat());

            if (drawMode.useIndexBuffer()) {

                IndexBufferGenerator indexBufferGenerator = mesh.getDrawMode().indexBufferGenerator();

                com.ferra13671.cometrenderer.buffer.GpuBuffer indexBuffer = mesh.getIndexBuffer();
                if (indexBuffer.getTarget() != BufferTarget.ELEMENT_ARRAY_BUFFER)
                    CometRenderer.manageException(new WrongGpuBufferTargetException(indexBuffer.getTarget().glId, BufferTarget.ELEMENT_ARRAY_BUFFER.glId));
                indexBuffer.bind();

                GL11.glDrawElements(drawMode.glId(), mesh.getIndexCount(), indexBufferGenerator.getIndexType().glId, 0);
            } else
                GL11.glDrawArrays(drawMode.glId(), 0, vertexCount);
        }
        if (close)
            mesh.close();
    };

    /**
     * Инициализирует CometRenderer.
     */
    public static void init() {
        if (registry.contains(CometTags.INITIALIZED))
            throw new IllegalStateException("CometRenderer has already initialized.");

        initRegistry();

        if (config.CHECK_OPENGL_VERSION.getValue()) {
            GLVersion glVersion = registry.get(CometTags.GL_VERSION).orElseThrow().getValue();
            if (glVersion.id < GLVersion.GL32.id)
                manageException(new UnsupportedOpenGLVersionException(glVersion, GLVersion.GL32));
        }

        samplerManager = registry.get(CometTags.SAMPLER_OBJECT_SUPPORT).orElseThrow().getValue() ?
                new SamplerManagerImpl()
                :
                new EmptySamplerManager();

        registry.setImmutable(CometTags.INITIALIZED, true);
    }

    private static void initRegistry() {
        registry.setImmutable(CometTags.COMET_RENDERER_VERSION, "2.2");

        String vendor = GL11.glGetString(GL11.GL_VENDOR);
        String version = GL11.glGetString(GL11.GL_VERSION);

        registry.setImmutable(CometTags.VENDOR, vendor);
        registry.setImmutable(CometTags.GPU, GL11.glGetString(GL11.GL_RENDERER));
        registry.setImmutable(CometTags.GL_VERSION, GLVersion.fromString(version));
        registry.setImmutable(CometTags.MESA_VERSION, Mesa3DVersion.fromString(version, vendor));
        registry.setImmutable(CometTags.MAX_VERTEX_ELEMENTS, GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS));
        registry.setImmutable(CometTags.SAMPLER_OBJECT_SUPPORT, registry.get(CometTags.GL_VERSION).orElseThrow().getValue().id >= GLVersion.GL33.id);

        int numExtensions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        String[] extensions = new String[numExtensions];
        for (int i = 0; i < numExtensions; i++)
            extensions[i] = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
        registry.setImmutable(CometTags.GL_EXTENSIONS, extensions);

        registry.set(CometTags.EXCEPTION_PROVIDER, CometRenderer::throwOrLogException);
    }

    public static void manageException(CometException exception) {
        registry.get(CometTags.EXCEPTION_PROVIDER).orElseThrow().getValue().manageException(exception);
    }

    public static void throwOrLogException(CometException exception) {
        if (config.DONT_THROW_EXCEPTIONS.getValue())
            logger.error(exception.getClass().getName().concat(": ".concat(exception.getMessage())));
        else
            throw exception;
    }

    /**
     * Устанавливает глобальный шейдерный цвет по умолчанию.
     */
    public static void resetShaderColor() {
        setShaderColor(new Vector4f(1f, 1f, 1f, 1f));
    }

    /**
     * Устанавливает глобальный шейдерный цвет в униформу, если программа использует фрагмент программы для глобального шейдерного цвета.
     *
     * @see CometRenderer#colorSnippet
     */
    public static void initShaderColor() {
        globalProgram.consumeIfUniformPresent(
                "shaderColor",
                UniformType.VEC4,
                colorUniform ->
                        colorUniform.set(getShaderColor())
        );
    }

    /**
     * Включает смешивание, если оно выключено, и устанавливает ему основные множители уравнения.
     *
     * @see <a href="https://docs.gl/gl4/glBlendFunc">OpenGL glBlendFunc wiki</a>
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void applyDefaultBlend() {
        State.BLEND.enable();
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
        State.BLEND.enable();
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
        State.BLEND.enable();
        GL14.glBlendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);
    }

    /**
     * Отключает смешивание, если оно включено.
     *
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    public static void disableBlend() {
        State.BLEND.disable();
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
     * Вызывает цикл отрисовки для меша и автоматически закрывает его.
     *
     * @param mesh меш.
     *
     * @see IMesh
     */
    public static void draw(IMesh mesh) {
        draw(COMET_BUFFER_RENDERER, mesh, true);
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
        draw(COMET_BUFFER_RENDERER, mesh, close);
    }

    /**
     * Вызывает цикл отрисовки при помощи данного отрисовщика и буффера вершин.
     * Так же закрывает данный буффер вершин по вашему желанию.
     *
     * @param bufferRenderer отрисовщик буффера вершин.
     * @param buffer буффер вершин.
     * @param close закрывать после отрисовки буффер вершин или нет.
     * @param <T> тип буффера вершин.
     */
    public static <T> void draw(BufferRenderer<T> bufferRenderer, T buffer, boolean close) {
        if (!scissorStack.isEmpty()) {
            State.SCISSOR.enable();
            scissorStack.peek().bind();
        } else
            State.SCISSOR.disable();

        globalProgram.bind();

        bufferRenderer.draw(buffer, close);

        globalProgram.unbind();
    }
}
