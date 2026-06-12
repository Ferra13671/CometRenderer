package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.ExceptionManager;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.sampler.ISamplerManger;
import com.ferra13671.cometrenderer.sampler.empty.EmptySamplerManager;
import com.ferra13671.cometrenderer.sampler.impl.SamplerManagerImpl;
import com.ferra13671.cometrenderer.utils.*;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.exceptions.impl.UnsupportedOpenGLVersionException;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.scissor.ScissorStack;
import com.ferra13671.cometrenderer.utils.stencil.StencilFunction;
import com.ferra13671.cometrenderer.utils.stencil.StencilOp;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.manager.ARBVertexFormatBufferManager;
import com.ferra13671.cometrenderer.vertex.format.manager.DefaultVertexFormatBufferManager;
import com.ferra13671.cometrenderer.vertex.format.manager.VertexFormatManager;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import com.ferra13671.cometrenderer.vertex.mesh.IMeshBuilder;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.*;

import java.util.function.Consumer;

/**
 * Главный класс рендеринга.
 * Контролирует всю работу рендера.
 */
@API(status = API.Status.STABLE, since = "1.1")
@UtilityClass
public class CometRenderer {
    /** Реестр различных данных. **/
    @Getter
    @API(status = API.Status.STABLE, since = "1.9")
    private final Registry registry = new Registry();
    /** Конфиг с различными настройками. **/
    @Getter
    @API(status = API.Status.STABLE, since = "1.9")
    private final Config config = new Config();
    /** Стек глобального шейдерного цвета, позволяющего контролировать цвет выходных объектов рендеринга, если программа реализовала данную возможность. **/
    @Getter
    @API(status = API.Status.MAINTAINED, since = "2.5")
    private final ShaderColor shaderColor = new ShaderColor();
    /** Фрагмент программы, необходимый для программ, которые хотят реализовать использование глобального шейдерного цвета. **/
    @Getter
    @API(status = API.Status.STABLE, since = "1.1")
    private final GlProgramSnippet colorSnippet = CometLoaders.IN_JAR.createProgramBuilder()
            .uniform("shaderColor", UniformType.VEC4)
            .buildSnippet();
    /** Текущая активная программа для CometRenderer'а, которая будет использоваться для отрисовки. **/
    @Getter
    @Setter
    @API(status = API.Status.STABLE, since = "2.6")
    private GlProgram currentProgram;
    /** Стек для областей, используемых ножницами. **/
    @Getter
    @API(status = API.Status.STABLE, since = "1.1")
    private final ScissorStack scissorStack = new ScissorStack();
    @Getter
    @API(status = API.Status.MAINTAINED, since = "2.1")
    private ISamplerManger samplerManager;
    @Getter
    @API(status = API.Status.INTERNAL, since = "2.5")
    private VertexFormatManager vertexFormatManager;
    @Getter
    @API(status = API.Status.MAINTAINED, since = "2.5")
    private final ExceptionManager exceptionManager = new ExceptionManager();
    /** Логгер CometRender'a, используемый для отправки ошибок. **/
    @Getter
    @Setter
    @API(status = API.Status.MAINTAINED, since = "2.1")
    private Logger logger = new Logger() {
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
    @API(status = API.Status.INTERNAL, since = "2.0")
    private final BufferRenderer<IMesh> COMET_BUFFER_RENDERER = (mesh, close) -> {
        int vertexCount = mesh.getVertexCount();

        if (vertexCount > 0) {
            DrawMode drawMode = mesh.getDrawMode();
            vertexFormatManager.applyFormatToBuffer(mesh.getVertexBuffer(), mesh.getVertexFormat());

            if (drawMode.useIndexBuffer()) {
                GpuBuffer indexBuffer = mesh.getIndexBuffer();
                if (indexBuffer.getTarget() != BufferTarget.ELEMENT_ARRAY_BUFFER)
                    exceptionManager.manageException(new WrongGpuBufferTargetException(indexBuffer.getTarget().glId, BufferTarget.ELEMENT_ARRAY_BUFFER.glId));
                indexBuffer.bind();

                GL11.glDrawElements(drawMode.glId(), mesh.getIndexCount(), mesh.getDrawMode().indexBufferGenerator().getIndexType().glId, 0);
            } else
                GL11.glDrawArrays(drawMode.glId(), 0, vertexCount);
        }
        if (close)
            mesh.close();
    };

    /**
     * Инициализирует CometRenderer.
     */
    @API(status = API.Status.STABLE, since = "2.0")
    public void init() {
        if (registry.contains(CometTags.INITIALIZED))
            throw new IllegalStateException("CometRenderer has already initialized.");

        initRegistry();
        config.MAX_VERTICES.setValue(registry.get(CometTags.MAX_VERTICES).orElseThrow());
        config.MAX_INDICES.setValue(registry.get(CometTags.MAX_INDICES).orElseThrow());

        if (config.CHECK_OPENGL_VERSION.getValue()) {
            if (!GLCapabilities.supportsVersion(GLVersion.fromId(config.MINIMUM_OPENGL_VERSION.getValue())))
                exceptionManager.manageException(new UnsupportedOpenGLVersionException(registry.get(CometTags.GL_VERSION).orElseThrow(), GLVersion.GL32));
        }

        samplerManager = GLCapabilities.supportsSamplerObjects() ?
                new SamplerManagerImpl()
                :
                new EmptySamplerManager();

        vertexFormatManager = GLCapabilities.supportsVertexAttributeBindings() ?
                new ARBVertexFormatBufferManager()
                :
                new DefaultVertexFormatBufferManager();

        registry.setImmutable(CometTags.INITIALIZED, true);
    }

    private void initRegistry() {
        registry.setImmutable(CometTags.COMET_RENDERER_VERSION, "2.7");

        String vendor = GL11.glGetString(GL11.GL_VENDOR);
        String version = GL11.glGetString(GL11.GL_VERSION);

        registry.setImmutable(CometTags.VENDOR, vendor);
        registry.setImmutable(CometTags.GPU, GL11.glGetString(GL11.GL_RENDERER));
        registry.setImmutable(CometTags.GL_VERSION, GLVersion.fromString(version));
        registry.setImmutable(CometTags.MESA_VERSION, Mesa3DVersion.fromString(version, vendor));
        registry.setImmutable(CometTags.MAX_VERTEX_ELEMENTS, GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS));
        registry.setImmutable(CometTags.MAX_VERTICES, GL11.glGetInteger(GL12.GL_MAX_ELEMENTS_VERTICES));
        registry.setImmutable(CometTags.MAX_INDICES, GL11.glGetInteger(GL12.GL_MAX_ELEMENTS_INDICES));

        int numExtensions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        String[] extensions = new String[numExtensions];
        for (int i = 0; i < numExtensions; i++)
            extensions[i] = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
        registry.setImmutable(CometTags.GL_EXTENSIONS, extensions);
    }

    /**
     * Устанавливает глобальный шейдерный цвет в униформу, если программа использует фрагмент программы для глобального шейдерного цвета.
     *
     * @see CometRenderer#colorSnippet
     */
    @API(status = API.Status.STABLE, since = "2.6")
    public void applyShaderColorUniform() {
        currentProgram.consumeIfUniformPresent(
                "shaderColor",
                UniformType.VEC4,
                colorUniform ->
                        colorUniform.set(getShaderColor().getColor())
        );
    }

    /**
     * Включает смешивание, если оно выключено, и устанавливает ему основные множители уравнения.
     *
     * @see <a href="https://docs.gl/gl4/glBlendFunc">OpenGL glBlendFunc wiki</a>
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setDefaultBlend() {
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
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setBlend(SrcFactor srcFactor, DstFactor dstFactor) {
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
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setBlend(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        State.BLEND.enable();
        GL14.glBlendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);
    }

    /**
     * Отключает смешивание, если оно включено.
     *
     * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void disableBlend() {
        State.BLEND.disable();
    }

    @API(status = API.Status.EXPERIMENTAL, since = "2.9")
    public void setStencil(StencilInfo stencil) {
        State.STENCIL.enable();

        if (stencil.stencilMask() != null) {
            if (stencil.stencilMask())
                State.STENCIL.enableMask();
            else
                State.STENCIL.disableMask();
        }
        if (stencil.depthMask() != null) {
            if (stencil.depthMask())
                State.DEPTH_TEST.enableMask();
            else
                State.DEPTH_TEST.disableMask();
        }
        ColorMask colorMask = stencil.colorMask();
        if (colorMask != null)
            State.COLOR_MASK.colorMask(colorMask.red(), colorMask.green(), colorMask.blue(), colorMask.alpha());
        StencilFunction function = stencil.func();
        if (function != null)
            State.STENCIL.function(function.function(), function.ref(), function.mask());
        StencilOp op = stencil.op();
        if (op != null)
            State.STENCIL.op(op.stencilFailed(), op.stencilPassedDepthFailed(), op.allPassed());
    }

    @API(status = API.Status.EXPERIMENTAL, since = "2.9")
    public void disableStencil() {
        State.STENCIL.disable();
    }

    @API(status = API.Status.EXPERIMENTAL, since = "2.9")
    public void clearStencil(int clearStencil) {
        State.STENCIL.enableMask();
        GL11.glClearStencil(clearStencil);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
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
    @API(status = API.Status.STABLE, since = "1.7")
    public IMesh createMesh(DrawMode drawMode, VertexFormat vertexFormat, Consumer<IMeshBuilder> buildConsumer) {
        IMeshBuilder meshBuilder = Mesh.builder(drawMode, vertexFormat);
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
    @API(status = API.Status.STABLE, since = "1.7")
    public void draw(IMesh mesh) {
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
    @API(status = API.Status.STABLE, since = "1.7")
    public void draw(IMesh mesh, boolean close) {
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
    @API(status = API.Status.MAINTAINED, since = "1.7")
    public <T> void draw(BufferRenderer<T> bufferRenderer, T buffer, boolean close) {
        if (!scissorStack.isEmpty()) {
            State.SCISSOR.enable();
            scissorStack.peek().bind();
        } else
            State.SCISSOR.disable();

        currentProgram.bind();

        bufferRenderer.draw(buffer, close);

        currentProgram.unbind();
    }
}
