package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.device.GLDevice;
import com.ferra13671.cometrenderer.stencil.StencilInfo;
import com.ferra13671.cometrenderer.utils.*;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.scissor.ScissorStack;
import com.ferra13671.cometrenderer.stencil.StencilFunction;
import com.ferra13671.cometrenderer.stencil.StencilOp;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.index.IndexBufferGenerator;
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
 * Главный класс рендерера CometRenderer.
 * <p>
 * <b>CometRenderer</b> — главная точка входа в библиотеку. Через него проходит
 * инициализация, отрисовка, создание мешей и изменение глобального состояние
 * конвейера (blend, depth test, scissor, stencil).
 * <p>
 * <b>Конфиг</b>. Перед инициализацией можно настроить конфигурацию, которую
 * можно получить через {@link #getConfig()}
 * <p>
 * <b>Инициализация</b>. Перед началом работы с библиотекой её сначала нужно
 * инициализировать. Для этого вызывается метод {@link #init()}.
 * Перед вызовом инициализацию большая часть библиотеки недоступна к
 * использованию.
 * <p>
 * <b>Реестр.</b> При инициализации в реестр (получаемый через {@link #getRegistry()})
 * записываются неизменяемые сведения о текущей среде: GPU, версия OpenGL,
 * версия Mesa3D, список расширений и лимиты вершин/индексов.
 * <p>
 * <b>Управление состоянием</b>. Все основные переключения GL состояний (blend, scissor,
 * stencil, depth test, program, framebuffer, viewport, textures, samplers) проходят
 * через {@link com.ferra13671.cometrenderer.device.state.PipelineStateManager}.
 * Использование {@code glEnable}/{@code glDisable}/{@code glBind...} напрямую не производятся.
 * В основном вы не контактируете с {@link com.ferra13671.cometrenderer.device.state.PipelineStateManager}
 * напрямую, т.к. он является низкоуровневым API.
 * <p>
 * <b>Программы и униформы</b>. Активная программа устанавливается через
 * {@link #setCurrentProgram(GLProgram)} и применяется отложено —
 * реальное передача программы в OpenGl и обновление униформ происходит
 * в методе {@link #draw}. До вызова {@link #draw} можно настраивать
 * униформы.
 * <p>
 * <b>Глобальный шейдерный цвет</b>. Если в программе есть униформа {@code shaderColor}
 * с типом {@link UniformType#VEC4} (либо подключил {@link #getColorSnippet()}), то в неё
 * можно передать текущий глобальный шейдерный цвет, используя метод {@link #applyShaderColorUniform()}.
 * Сама настройка шейдерного цвета происходит через объект, получаемый через {@link #getShaderColor()}.
 * <p>
 * <b>Потокобезопасность</b>. Класс по большей части не является потокобезопасным. Т.е. все
 * вызовы должны происходить из потока, владеющего GL-контекстом (есть некоторые исключения).
 */
@API(status = API.Status.STABLE, since = "1.1")
@UtilityClass
public class CometRenderer {
    /**
     * Реестр изменяемых и неизменяемых сведений о среде выполнения и значений внутри рендерера.
     * <p>
     * После вызова {@link #init()} содержит: версию рендерера, вендора, название GPU, версию OpenGL,
     * версию Mesa3D, список GL-расширений, максимальное число вершин и индексов, а также флаг инициализации.
     * <p>
     * Значения, помеченные как immutable, не могут быть больше изменены или удалены после их добавления.
     * Попытка перезаписи таких значений приведёт к {@link UnsupportedOperationException}
     *
     * @see Registry
     * @see CometTags
     */
    @Getter
    @API(status = API.Status.STABLE, since = "1.9")
    private final Registry registry = new Registry();
    /**
     * Пользовательский конфиг рендерера.
     * <p>
     * Содержит настройки, влияющие на поведение {@link #init()} и создание ресурсов:
     * минимальная версия OpenGL, проверять ли версию, размер аллокатора меша по умолчанию, максимальное
     * число вершин и индексов.
     * <p>
     * Значение {@link Config#MAX_VERTICES} и {@link Config#MAX_INDICES}
     */
    @Getter
    @API(status = API.Status.STABLE, since = "1.9")
    private final Config config = new Config();
    /**
     * Стек глобального шейдерного цвета.
     * <p>
     * Позволяет настроить глобальный цвет, который можно будет применить ко всем программам, имеющим
     * есть униформа {@code shaderColor} или подключившие {@link #getColorSnippet()} фрагмент.
     * <p>
     * Реализован как стек — удобно для вложенных областей рендеринга, где
     * цвет меняется на время, а затем возвращается к предыдущему.
     * <p>
     * Что бы применить цвет к активной программе, вызовите {@link #applyShaderColorUniform()}.
     *
     * @see ShaderColor
     * @see #applyShaderColorUniform()
     */
    @Getter
    @API(status = API.Status.MAINTAINED, since = "2.5")
    private final ShaderColor shaderColor = new ShaderColor();
    /**
     * Фрагмент программы, добавляющий униформу {@code shaderColor}.
     * <p>
     * Программы, которые хотят поддерживать глобальный шейдерный цвет ({@link #getShaderColor()}),
     * должны подключать этот фрагмент в
     * {@link com.ferra13671.cometrenderer.glsl.GLProgramBuilder}.
     * <p>
     * Однако метод {@link #applyShaderColorUniform()} не требует явного подключения этого фрагмента к программе,
     * поэтому вместо этого вы также можете просто добавить униформу с именем {@code shaderColor} и типом
     * {@link UniformType#VEC4}.
     *
     * @see GLProgramSnippet
     * @see #applyShaderColorUniform()
     */
    @Getter
    @API(status = API.Status.STABLE, since = "1.1")
    private final GLProgramSnippet colorSnippet = GLProgram.builder()
            .uniform("shaderColor", UniformType.VEC4)
            .buildSnippet();
    /**
     * Стек областей ножниц.
     * <p>
     * Верхняя область из стека автоматически применяется при каждом вызове {@link #draw}.
     * Если стек пуст, scissor-тест отключается.
     * <p>
     * Если при добавлении области в стек он не был пуст, но в стек
     * будет добавлена область, полученная пересечением прошлой области и новой. Это позволяет
     * вкладывать области: например, вырезать прямоугольник, внутри него — ещё один, и после
     * завершения вернутся к внешнему.
     *
     * @see ScissorStack
     */
    @Getter
    @API(status = API.Status.STABLE, since = "1.1")
    private final ScissorStack scissorStack = new ScissorStack();
    /**
     * Низкоуровневое устройство GL.
     * <p>
     * <b>Низкоуровневое API</b>. Не рекомендован к использованию обычному пользователю.
     * Прямые вызовы допускаются только в библиотеке.
     * <p>
     * До вызова {@link #init()} равен {@code null}. Любое обращение до инициализации
     * приведёт к {@link NullPointerException}.
     *
     * @see GLDevice
     */
    @Getter
    @API(status = API.Status.INTERNAL, since = "2.9")
    private GLDevice device;
    /**
     * Логгер библиотеки. Используется {@link ErrorHandlers} и другими подсистемами
     * для вывода информации, предупреждений и ошибок.
     * <p>
     * По умолчанию пишет в {@link System#out} / {@link System#err}. Может быть заменён
     * пользователем через {@link #setLogger(Logger)}.
     *
     * @see Logger
     */
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
    /**
     * Отрисовщик по умолчанию для {@link IMesh}.
     * <p>
     * Применяет формат вершин к буферу через {@link com.ferra13671.cometrenderer.device.vertexformat.VertexFormatManager},
     * биндит индексный буфер (если {@link DrawMode} имеет генератор индексов) и вызывает
     * {@link GL11#glDrawElements} или {@link GL11#glDrawArrays}. По флагу {@code close} закрывает меш после
     * отрисовки.
     * <p>
     * Меш не отрисовывается, если вершин нет (хотя в основной реализации ({@link Mesh}) этого не может быть),
     * но меш всё также может быть закрыт.
     */
    @API(status = API.Status.INTERNAL, since = "2.0")
    private final BufferRenderer<IMesh> cometBufferRenderer = (mesh, close) -> {
        int vertexCount = mesh.getVertexCount();

        if (vertexCount > 0) {
            DrawMode drawMode = mesh.getDrawMode();
            device.getVertexFormatManager().applyFormatToBuffer(mesh.getVertexBuffer(), mesh.getVertexFormat());

            IndexBufferGenerator ibg = drawMode.indexBufferGenerator();

            if (ibg != null) {
                GpuBuffer indexBuffer = ibg.getIndexBuffer(mesh.getIndexCount());

                if (indexBuffer.getTarget() != BufferTarget.ELEMENT_ARRAY_BUFFER)
                    ErrorHandlers.onWrongBufferTarget(indexBuffer.getTarget().glId, BufferTarget.ELEMENT_ARRAY_BUFFER.glId);
                else
                    indexBuffer.bind();

                GL11.glDrawElements(drawMode.glId(), mesh.getIndexCount(), ibg.getIndexType().glId, 0);
            } else
                GL11.glDrawArrays(drawMode.glId(), 0, vertexCount);
        }
        if (close)
            mesh.close();
    };

    /**
     * Инициализирует CometRenderer.
     * <p>
     * Выполняет следующие действия:
     * <ul>
     *     <li>Проверяет, что рендерер ещё не инициализирован (иначе — исключение);</li>
     *     <li>Заполняет регистр данными о среде;</li>
     *     <li>Переносит лимиты {@link CometTags#MAX_VERTICES} и {@link CometTags#MAX_INDICES} в {@link #getConfig()};</li>
     *     <li>Проверяет поддержку минимальной версии OpenGL, если {@link Config#CHECK_OPENGL_VERSION} включён;</li>
     *     <li>Создаёт {@link GLDevice}.</li>
     * </ul>
     *
     * <b>Важно:</b> должен быть вызван после создания и активации GL-контекста,
     * но до любого другого обращения к рендереру.
     *
     * @throws IllegalStateException если рендерер уже инициализирован.
     * @throws RuntimeException если текущий OpenGL не поддерживает минимально
     *         требуемую версию (см. {@link Config#CHECK_OPENGL_VERSION}).
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
                ErrorHandlers.onUnsupportedOpenGLVersion(registry.get(CometTags.GL_VERSION).orElseThrow(), GLVersion.GL33);
        }

        device = new GLDevice();

        registry.setImmutable(CometTags.INITIALIZED, true);
    }

    private void initRegistry() {
        registry.setImmutable(CometTags.COMET_RENDERER_VERSION, "3.0");

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
     * Возвращает программу, выбранную в качестве текущей.
     * <p>
     * Программа становится текущей после вызова {@link #setCurrentProgram},
     * но реальный {@code glUseProgram} происходит позже, во время {@link #draw}.
     * Этот метод возвращает именно «логически выбранную» программу, а не ту,
     * что сейчас активна на уровне GL.
     * <p>
     * Может вернуть {@code null}, если {@link #setCurrentProgram} ещё не вызывался.
     *
     * @return текущая программа или {@code null}
     * @see #setCurrentProgram(GLProgram)
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public GLProgram getCurrentProgram() {
        return device.getCurrentProgram();
    }

    /**
     * Логически выбирает программу для следующих {@link #draw}.
     * <p>
     * Реальный бинд программы в GL происходит в {@link #draw}. Это позволяет настраивать
     * униформы программы до её фактического бинда.
     * <p>
     * Если программа уже была текущей, то логически вызов не будет no-op, но повторного
     * бинда в GL не будет, т.к. состояние кешируется в {@link com.ferra13671.cometrenderer.device.state.PipelineStateManager}.
     *
     * @param program программа, которая будет использоваться для следующих draw.
     *
     * @see #getCurrentProgram()
     * @see GLProgram#bind()
     * @see #draw
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setCurrentProgram(GLProgram program) {
        device.setCurrentProgram(program);
    }

    /**
     * Устанавливает глобальный шейдерный цвет в униформу текущей программы.
     * <p>
     * Вызов данного метода безопасен, т.к. если необходимой униформы нет, вызов
     * ничего не делает.
     * <p>
     * Значение берётся из {@link #getShaderColor()} — верхнего элемента стека глобального
     * шейдерного цвета.
     *
     * @see CometRenderer#colorSnippet
     */
    @API(status = API.Status.STABLE, since = "2.6")
    public void applyShaderColorUniform() {
        getCurrentProgram().consumeIfUniformPresent(
                "shaderColor",
                UniformType.VEC4,
                colorUniform ->
                        colorUniform.set(getShaderColor().getColor())
        );
    }

    /**
     * Включает смешивание с множителями по умолчанию.
     * <p>
     * Устанавливает стандартную формулу смешивания:
     * {@link SrcFactor#SRC_ALPHA} / {@link DstFactor#ONE_MINUS_SRC_ALPHA} для RGB и альфы.
     * Это подходит для большинства случаев отрисовки полупрозрачных текстур и примитивов.
     * <p>
     * Если blend уже включен с этими множителями, вызов — no-op.
     *
     * @see #setBlend
     * @see #disableBlend
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setDefaultBlend() {
        device.getPipelineStateManager().setBlend(true);
        device.getPipelineStateManager().setBlendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Включает смешивание с одинаковыми множителями для RGB и альфы.
     * <p>
     * Эквивалентен вызову {@link #setBlend(SrcFactor, DstFactor, SrcFactor, DstFactor)}.
     * <p>
     * Если blend уже включен с этими множителями, вызов — no-op.
     *
     * @param srcFactor множитель для нового цвета и альфы.
     * @param dstFactor множитель для цвета и альфы в фреймбуфере.
     *
     * @see #setBlend(SrcFactor, DstFactor, SrcFactor, DstFactor)
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setBlend(SrcFactor srcFactor, DstFactor dstFactor) {
        device.getPipelineStateManager().setBlend(true);
        device.getPipelineStateManager().setBlendFunc(srcFactor, dstFactor, srcFactor, dstFactor);
    }

    /**
     * Включает смешивание с раздельными множителями для RGB и альфы.
     * <p>
     * Используйте, если для цвета и альфы нужны разные формулы смешивания.
     * <p>
     * Если blend уже включен с этими множителями, вызов — no-op.
     *
     * @param srcColor множитель для нового цвета.
     * @param dstColor множитель для цвета в фреймбуфере.
     * @param srcAlpha множитель для новой альфы.
     * @param dstAlpha множитель для альфы в фреймбуфере.
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void setBlend(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        device.getPipelineStateManager().setBlend(true);
        device.getPipelineStateManager().setBlendFunc(srcColor, dstColor, srcAlpha, dstAlpha);
    }

    /**
     * Отключает смешивание.
     * <p>
     * Если blend уже выключен, вызов — no-op. Множители для цвета и альфы не сбрасываются:
     * при следующем включении blend с этими же параметрами просто включит blend.
     *
     * @see #setBlend
     * @see #setDefaultBlend
     */
    @API(status = API.Status.MAINTAINED, since = "2.6")
    public void disableBlend() {
        device.getPipelineStateManager().setBlend(false);
    }

    /**
     * Включает stencil-тест и применяет нужный набор параметров из пресета {@link StencilInfo}.
     * <p>
     * <b>Побочный эффект</b>. Пресеты также затрагивают не только stencil-состояние, но и
     * маски color/depth. Это необходимо для правильной работы пресетов, но если вам нужны только stencil-параметры
     * — создайте свой собственный пресет {@link StencilInfo} или используйте низкоуровневый
     * API ({@link com.ferra13671.cometrenderer.device.state.PipelineStateManager}).
     *
     * @param stencil пресет stencil-параметров.
     *
     * @see StencilInfo
     * @see #disableStencil
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setStencil(StencilInfo stencil) {
        device.getPipelineStateManager().setStencil(true);

        if (stencil.stencilMask() != null)
            device.getPipelineStateManager().setStencilMask(stencil.stencilMask());

        if (stencil.depthMask() != null)
            device.getPipelineStateManager().setDepthMask(stencil.depthMask());

        ColorMask colorMask = stencil.colorMask();
        if (colorMask != null)
            device.getPipelineStateManager().setColorMask(colorMask.red(), colorMask.green(), colorMask.blue(), colorMask.alpha());

        StencilFunction function = stencil.func();
        if (function != null)
            device.getPipelineStateManager().setStencilFunc(function.function(), function.ref(), function.mask());

        StencilOp op = stencil.op();
        if (op != null)
            device.getPipelineStateManager().setStencilOp(op.stencilFailed(), op.stencilPassedDepthFailed(), op.allPassed());
    }

    /**
     * Отключает stencil-тест.
     * <p>
     * Stencil-параметры (функция сравнения, операции, <b>а также маски</b>) не сбрасываются.
     *
     * @see #setStencil(StencilInfo)
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void disableStencil() {
        device.getPipelineStateManager().setStencil(false);
    }

    /**
     * Очищает stencil-буфер заданным значением.
     * <p>
     * <b>Побочный эффект</b>. При очищении включается маска записи stencil-буфера.
     * Это необходимо, т.к. без этого невозможно очистить буфер. Однако стоит помнить,
     * что это может сломать некоторые stencil пресеты.
     * <b>Рекомендуется вызывать этот метод только если stencil-тест выключен, либо
     * выбран пресет {@link StencilInfo#WRITE}.</b>
     * <p>
     * Не изменяет другие аспекты stencil-теста.
     *
     * @param clearStencil значение, которым будет заполнен stencil-буфер
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void clearStencil(int clearStencil) {
        device.getPipelineStateManager().setStencilMask(true);
        GL11.glClearStencil(clearStencil);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }

    /**
     * Включает тест глубины.
     * <p>
     * Если тест уже включен, вызов — no-op. Маска и функция не изменяются.
     *
     * @see #disableDepthTest
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void enableDepthTest() {
        device.getPipelineStateManager().setDepthTest(true);
    }

    /**
     * Выключает тест глубины.
     * <p>
     * Если тест уже выключен, вызов — no-op. Маска и функция не изменяются.
     *
     * @see #enableDepthTest
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void disableDepthTest() {
        device.getPipelineStateManager().setDepthTest(false);
    }

    /**
     * Создаёт меш с вершинами.
     * <p>
     * Метод создаёт сборщик, передаёт его в {@code buildConsumer}, и собирает меш.
     * Если вы не передали ни одной вершины в билдер — вернётся {@code null}.
     * <p>
     * Порядок элементов должны соответствовать {@link VertexFormat}.
     *
     * @param drawMode тип отрисовки вершин.
     * @param vertexFormat формат вершины.
     * @param buildConsumer метод для добавления вершин в сборщик.
     * @return готовый меш или {@code null}, если в сборщике нет вершин.
     */
    @API(status = API.Status.STABLE, since = "1.7")
    public IMesh createMesh(DrawMode drawMode, VertexFormat vertexFormat, Consumer<IMeshBuilder> buildConsumer) {
        IMeshBuilder meshBuilder = Mesh.builder(drawMode, vertexFormat);
        buildConsumer.accept(meshBuilder);
        return meshBuilder.buildNullable();
    }

    /**
     * Отрисовывает меш и закрывает его после отрисовки.
     * <p>
     * Эквивалентен {@code draw(mesh, true)}.
     * <p>
     * <b>Требует установленной текущей программы.</b> Если
     * {@link #setCurrentProgram(GLProgram)} не вызывался, при попытке бинда
     * программы произойдёт {@link NullPointerException}.
     *
     * @param mesh меш для отрисовки.
     *
     * @see IMesh
     * @see #draw(IMesh, boolean)
     * @see #draw(BufferRenderer, Object, boolean)
     */
    @API(status = API.Status.STABLE, since = "1.7")
    public void draw(IMesh mesh) {
        draw(cometBufferRenderer, mesh, true);
    }

    /**
     * Отрисовывает меш и по желанию закрывает его после отрисовки.
     * <p>
     * Если {@code close == true}, после отрисовки вызывается {@link IMesh#close()},
     * освобождающий GL-ресурсы меша. Если вы планируете рисовать меш повторно,
     * передайте {@code false} и закройте меш самостоятельно, когда он больше не нужен.
     * <p>
     * <b>Требует установленной текущей программы.</b> Если
     * {@link #setCurrentProgram(GLProgram)} не вызывался, при попытке бинда
     * программы произойдёт {@link NullPointerException}.
     *
     * @param mesh меш для отрисовки.
     * @param close закрывать ли меш после отрисовки.
     *
     * @see #draw(IMesh)
     * @see #draw(BufferRenderer, Object, boolean)
     */
    @API(status = API.Status.STABLE, since = "1.7")
    public void draw(IMesh mesh, boolean close) {
        draw(cometBufferRenderer, mesh, close);
    }

    /**
     * Отрисовывает произвольный буфер с помощью с помощью заданного отрисовщика.
     * <p>
     * Метод выполняет следующие действия:
     * <ol>
     *     <li>применяет текущую scissor область (или отключает scissor, если стек пуст);</li>
     *     <li>биндит текущую программу в GL а также загружает изменённые униформы;</li>
     *     <li>вызывает {@link BufferRenderer#draw}.</li>
     * </ol>
     * <p>
     * Позволяет использовать собственные буферы и стратегии отрисовки, не ограничиваясь
     * {@link IMesh}. Стандартный отрисовщик для мешей — {@link #cometBufferRenderer} —
     * доступен только через перегрузки {@link #draw(IMesh)} и {@link #draw(IMesh, boolean)}.
     * <p>
     * <b>Требует установки текущей программы.</b>
     *
     * @param bufferRenderer отрисовщик буффера.
     * @param buffer буффер для отрисовки.
     * @param close закрывать буффер после отрисовки.
     * @param <T> тип буффера.
     *
     * @see BufferRenderer
     * @see #setCurrentProgram
     * @see #getScissorStack()
     */
    @API(status = API.Status.MAINTAINED, since = "1.7")
    public <T> void draw(BufferRenderer<T> bufferRenderer, T buffer, boolean close) {
        if (!scissorStack.isEmpty()) {
            device.getPipelineStateManager().setScissor(true);
            scissorStack.peek().bind();
        } else
            device.getPipelineStateManager().setScissor(false);

        getCurrentProgram().bind();
        bufferRenderer.draw(buffer, close);
    }

    /**
     * Возвращает количество GL программ, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых программ.
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public int getProgramsCount() {
        return device.getProgramsCount();
    }

    /**
     * Возвращает количество GL шейдеров, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых шейдеров.
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public int getShadersCount() {
        return device.getShadersCount();
    }

    /**
     * Возвращает количество GL текстур, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых текстур.
     */
    @API(status = API.Status.EXPERIMENTAL, since = "3.0")
    public int getTexturesCount() {
        return device.getTexturesCount();
    }

    /**
     * Возвращает количество GL фреймбуферов, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых фреймбуферов.
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public int getFramebuffersCount() {
        return device.getFramebuffersCount();
    }

    /**
     * Возвращает количество GL семплеров, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых семплеров.
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public int getSamplersCount() {
        return device.getSamplersCount();
    }

    /**
     * Возвращает количество GL VAO, созданных через CometRenderer и не
     * закрытых на данных момент.
     * <p>
     * VAO обычно используется в {@link VertexFormat}, но количество VAO
     * обозначает только те {@link VertexFormat}, которые хотя бы раз
     * были использованы для отрисовки.
     * <p>
     * Используется для отладки.
     *
     * @return количество открытых VAO.
     */
    @API(status = API.Status.MAINTAINED, since = "3.0")
    public int getVertexArraysCount() {
        return device.getVertexArraysCount();
    }
}
