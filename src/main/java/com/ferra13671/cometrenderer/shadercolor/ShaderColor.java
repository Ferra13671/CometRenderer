package com.ferra13671.cometrenderer.shadercolor;

import org.apiguardian.api.API;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Глобальный шейдерный цвет с поддержкой стека и цветовых масок.
 * <p>
 * Класс хранит текущий глобальный цвет, который можно
 * передавать в униформу {@code shaderColor} (типа {@link com.ferra13671.cometrenderer.glsl.uniform.UniformType#VEC4})
 * активной GL-программы. Удобен для изменения оттенка множества объектов
 * через единый вызов — шейдер умножает свой цвет на переданный.
 * <p>
 * <b>Стек цвета</b>. Текущий цвет можно сохранять через {@link #push()}
 * и восстанавливать через {@link #pop()}. Это удобно для вложенных
 * областей рендеринга, где цвет меняется на время, а затем возвращается
 * к предыдущему.
 * <p>
 * <b>Цветовые маски</b>. Поверх стека цвета действует стек цветовых
 * масок ({@link #pushMask(Vector4fc)} / {@link #popMask()}). Маска —
 * это множитель, применяемый поэлементно к текущему цвету. Вложенные
 * маски перемножаются: {@code pushMask(0.5) + pushMask(0.5)} даёт
 * эффективную маску {@code 0.25}. Маски полезны для затемнения целых
 * групп объектов без изменения их базового цвета. Стек масок
 * независим от стека {@code push()}/{@code pop()}: вызовы
 * {@code push()}/{@code pop()} маски не сохраняют и не восстанавливают.
 * <p>
 * <b>Получение цвета</b>. {@link #getColorRef()} возвращает внутреннюю
 * ссылку без аллокации — используйте его в горячих путях (например,
 * для загрузки юниформы). {@link #getColor(Vector4f)} копирует цвет
 * в переданный буфер.
 * <p>
 * <b>Внимание</b>. Не передавайте копию результата {@link #getColor()}
 * в {@link #setColor(Vector4fc)} при активной маске — маска применится
 * дважды. Вызов {@code setColor(getColorRef())} безопасен: распознаётся
 * и не изменяет состояние.
 * <p>
 * <b>Потокобезопасность</b>. Класс не потокобезопасен. Один экземпляр
 * на поток/GL-контекст. При использовании из нескольких потоков
 * требуется внешняя синхронизация.
 *
 * @see com.ferra13671.cometrenderer.CometRenderer#getShaderColor()
 * @see com.ferra13671.cometrenderer.CometRenderer#applyShaderColorUniform()
 * @see com.ferra13671.cometrenderer.CometRenderer#getColorSnippet()
 */
@API(status = API.Status.MAINTAINED, since = "2.4")
public final class ShaderColor {
    private static final Vector4fc DEFAULT_COLOR = new Vector4f(1f, 1f, 1f, 1f);

    private final Deque<float[]> stack = new ArrayDeque<>();
    private final Deque<float[]> maskStack = new ArrayDeque<>();
    private final Vector4f colorWithoutMask = new Vector4f(DEFAULT_COLOR);
    private final Vector4f colorWithMask = new Vector4f(DEFAULT_COLOR);

    /**
     * Сохраняет текущий цвет в стек.
     * <p>
     * Парный метод — {@link #pop()}, восстанавливающий цвет. Стек цвета
     * независим от стека масок: {@code push()}/{@code pop()} не затрагивают
     * маски, и наоборот.
     * <p>
     * <b>Важно:</b> каждый вызов {@code push()} должен быть сбалансирован
     * вызовом {@link #pop()}. При {@code pop()} на пустом стеке будет
     * выброшено {@link IllegalStateException}.
     *
     * @see #pop()
     */
    public void push() {
        this.stack.push(createArray(this.colorWithoutMask));
    }

    /**
     * Устанавливает текущий цвет (без учёта маски).
     * <p>
     * Метод задаёт базовый цвет, к которому затем применяется верхняя
     * маска стека (если она есть). Если маски нет — результат
     * {@link #getColorRef()} равен {@code shaderColor}.
     * <p>
     * <b>Внимание:</b> не передавайте сюда копию результата {@link #getColor()}
     * при активной маске — маска применится дважды. Безопасно использовать
     * {@code setColor(getColorRef())}: этот вызов распознаётся как no-op.
     *
     * @param shaderColor новый цвет; не должен быть {@code null}.
     *
     * @throws NullPointerException если {@code shaderColor == null}.
     *
     * @see #setColor(float, float, float, float)
     * @see #getColorRef()
     */
    public void setColor(Vector4fc shaderColor) {
        Objects.requireNonNull(shaderColor, "shaderColor");

        if (shaderColor == this.colorWithMask)
            return;

        this.colorWithoutMask.set(shaderColor);
        updateColorWithMask();
    }

    /**
     * Устанавливает текущий цвет (без учёта маски) из покомпонентных значений.
     * <p>
     * Эквивалентен {@link #setColor(Vector4fc)}, но не требует создания
     * временного {@link Vector4f}.
     *
     * @param red   красный канал.
     * @param green зелёный канал.
     * @param blue  синий канал.
     * @param alpha альфа-канал.
     *
     * @see #setColor(Vector4fc)
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public void setColor(float red, float green, float blue, float alpha) {
        this.colorWithoutMask.set(red, green, blue, alpha);
        updateColorWithMask();
    }

    /**
     * Возвращает копию текущего цвета (с учётом маски).
     *
     * @return новый {@link Vector4f} с текущим цветом.
     *
     * @deprecated используйте {@link #getColorRef()} (без аллокации) или
     *             {@link #getColor(Vector4f)} (копирование во внешний буфер).
     *             Метод будет удалён в следующих версиях.
     */
    @API(status = API.Status.DEPRECATED, since = "3.1")
    @Deprecated(forRemoval = true)
    public Vector4f getColor() {
        return getColor(new Vector4f());
    }

    /**
     * Копирует текущий цвет (с учётом маски) в переданный буфер.
     * <p>
     * В отличие от {@link #getColorRef()} не возвращает внутреннюю ссылку,
     * что безопасно для сохранения между мутациями, но требует внешнего
     * буфера. Используйте этот метод, если результат нужно сохранить
     * надолго; для немедленного чтения (например, загрузки юниформы)
     * предпочтителен {@link #getColorRef()}.
     *
     * @param dest буфер, в который будет записан цвет; не должен быть {@code null}.
     * @return {@code dest} — для цепочек вызовов.
     *
     * @throws NullPointerException если {@code dest == null}.
     *
     * @see #getColorRef()
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public Vector4f getColor(Vector4f dest) {
        Objects.requireNonNull(dest, "dest");

        return dest.set(getColorRef());
    }

    /**
     * Возвращает внутреннюю ссылку на текущий цвет (с учётом маски)
     * без копирования.
     * <p>
     * В отличие от {@link #getColor(Vector4f)} не создаёт объект и не
     * копирует данные — подходит для горячих путей (загрузка юниформ,
     * отрисовка).
     * <p>
     * <b>Время жизни.</b> Ссылка валидна до следующей мутации этого
     * {@link ShaderColor} ({@link #setColor}, {@link #resetColor},
     * {@link #pop}, {@link #pushMask}, {@link #popMask}). Не сохраняйте
     * её между кадрами и не используйте после мутации.
     * <p>
     * <b>Только для чтения.</b> Возвращаемый объект — {@link Vector4fc},
     * хотя фактически может быть {@link Vector4f}. Мутировать его
     * напрямую (приведением типа) нельзя — это нарушит инварианты
     * и приведёт к рассинхронизации {@code colorWithMask}.
     *
     * @return ссылка на текущий цвет (с учётом маски).
     *
     * @see #getColor(Vector4f)
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public Vector4fc getColorRef() {
        return this.maskStack.isEmpty() ? this.colorWithoutMask : this.colorWithMask;
    }

    /**
     * Сбрасывает базовый цвет в белый ({@code (1, 1, 1, 1)}).
     * <p>
     * Сбрасывается только базовый цвет, к которому затем применяется
     * текущая маска (если она есть). Стек {@link #push()} и стек
     * масок не изменяются.
     *
     * @see #setColor(Vector4fc)
     */
    public void resetColor() {
        this.colorWithoutMask.set(DEFAULT_COLOR);
        updateColorWithMask();
    }

    /**
     * Восстанавливает цвет, сохранённый последним вызовом {@link #push()}.
     * <p>
     * <b>Важно:</b> стек масок не затрагивается — если после {@code push()}
     * был вызван {@link #pushMask(Vector4fc)}, он останется активным.
     *
     * @throws IllegalStateException если стек цвета пуст
     *         (несбалансированный {@code push()}/{@code pop()}).
     *
     * @see #push()
     */
    public void pop() {
        if (this.stack.isEmpty())
            throw new IllegalStateException("Unbalanced ShaderColor.push()/pop()");

        this.colorWithoutMask.set(this.stack.pop());
        updateColorWithMask();
    }

    /**
     * Добавляет цветовую маску в стек масок.
     * <p>
     * Маска поэлементно умножается на текущую вершину стека масок
     * (если она есть), и результат становится новой вершиной. Таким
     * образом вложенные маски перемножаются. Текущий цвет
     * ({@link #getColorRef()}) пересчитывается с учётом новой маски.
     * <p>
     * Стек масок независим от стека {@link #push()}/{@link #pop()}:
     * маски не сохраняются и не восстанавливаются вызовами
     * {@code push()}/{@code pop()}.
     *
     * @param mask цветовая маска (обычно значения в диапазоне {@code [0, 1]});
     *             не должна быть {@code null}.
     *
     * @throws NullPointerException если {@code mask == null}.
     *
     * @see #pushMask(float, float, float, float)
     * @see #popMask()
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public void pushMask(Vector4fc mask) {
        Objects.requireNonNull(mask, "mask");

        pushMask(mask.x(), mask.y(), mask.z(), mask.w());
    }

    /**
     * Добавляет цветовую маску в стек масок из покомпонентных значений.
     * <p>
     * Эквивалентен {@link #pushMask(Vector4fc)}, но не требует создания
     * временного {@link Vector4f}.
     *
     * @param red   множитель красного канала.
     * @param green множитель зелёного канала.
     * @param blue  множитель синего канала.
     * @param alpha множитель альфа-канала.
     *
     * @see #pushMask(Vector4fc)
     * @see #popMask()
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public void pushMask(float red, float green, float blue, float alpha) {
        float[] prevMask = this.maskStack.peek();

        float[] m = new float[]{
                red,
                green,
                blue,
                alpha
        };
        if (prevMask != null) {
            m[0] *= prevMask[0];
            m[1] *= prevMask[1];
            m[2] *= prevMask[2];
            m[3] *= prevMask[3];
        }

        this.maskStack.push(m);
        updateColorWithMask();
    }

    /**
     * Удаляет верхнюю цветовую маску из стека.
     * <p>
     * Текущий цвет пересчитывается с учётом новой вершины стека
     * (или без маски, если стек пуст).
     *
     * @throws IllegalStateException если стек масок пуст
     *         (несбалансированный {@code pushMask()}/{@code popMask()}).
     *
     * @see #pushMask(Vector4fc)
     * @see #pushMask(float, float, float, float)
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public void popMask() {
        if (this.maskStack.isEmpty())
            throw new IllegalStateException("Unbalanced ShaderColor.pushMask()/popMask()");

        this.maskStack.pop();
        updateColorWithMask();
    }

    /**
     * Создаёт скоуп, в котором можно свободно менять базовый цвет через
     * {@link #setColor(Vector4fc)} / {@link #setColor(float, float, float, float)}
     * — по выходу из блока цвет автоматически восстановится.
     * <p>
     * Эквивалентен {@code push()} с автоматическим {@code pop()} при закрытии
     * скоупа. В отличие от {@link #scoped(Vector4fc)} не устанавливает новый
     * цвет — только сохраняет текущий как точку восстановления. Это позволяет
     * менять цвет несколько раз внутри блока и не думать о балансировке
     * стека вручную:
     * <pre>{@code
     * try (var ignored = color.scoped()) {
     *     color.setColor(1f, 0f, 0f, 1f);
     *     drawRed();
     *     color.setColor(0f, 1f, 0f, 1f);
     *     drawGreen();
     * }
     * // цвет восстановлен
     * }</pre>
     * <p>
     * <b>Вложенность.</b> Скоупы можно вкладывать — каждый сохраняет
     * предыдущий цвет и восстанавливает его при закрытии. Стек масок
     * скоуп не затрагивает: если внутри блока активирована маска, она
     * сохраняется независимо.
     * <p>
     * <b>Баланс стека.</b> Если внутри блока вызывается {@link #push()}
     * без парного {@link #pop()}, закрытие скоупа снимет именно этот
     * «лишний» кадр, а не свой собственный — стек окажется испорчен.
     * Балансируйте вложенные вызовы вручную.
     *
     * @return скоуп; закрытие восстанавливает цвет, сохранённый при создании.
     *
     * @see #scoped(Vector4fc)
     * @see ShaderColorScope
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public ShaderColorScope scoped() {
        push();
        return this::pop;
    }

    /**
     * Создаёт скоуп, в котором базовый цвет заменяется на заданный.
     * <p>
     * Эквивалентен {@code push() → setColor(color)} с автоматическим
     * {@code pop()} при закрытии скоупа. Удобен для временного изменения
     * цвета на время рендера группы объектов:
     * <pre>{@code
     * try (var ignored = color.scoped(new Vector4f(1f, 0f, 0f, 1f))) {
     *     drawRedStuff();
     * }
     * }</pre>
     * <p>
     * <b>Вложенность.</b> Скоупы можно вкладывать — каждый сохраняет
     * предыдущий цвет и восстанавливает его при закрытии. Если внутри
     * скоупа цвета также активируется маска, она сохраняется независимо
     * (см. {@link #scopedMask(org.joml.Vector4fc)}) и не сбрасывается
     * при закрытии скоупа цвета.
     * <p>
     * <b>Безопасность исключений.</b> Если {@code color} равен {@code null},
     * выбрасывается {@link NullPointerException} <i>до</i> сохранения
     * в стек — состояние {@link ShaderColor} остаётся неизменным.
     *
     * @param color новый цвет; не должен быть {@code null}.
     * @return скоуп; закрытие восстанавливает предыдущий базовый цвет.
     *
     * @throws NullPointerException если {@code color == null}.
     *
     * @see #scoped(float, float, float, float)
     * @see #scopedMask(org.joml.Vector4fc)
     * @see ShaderColorScope
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public ShaderColorScope scoped(Vector4fc color) {
        Objects.requireNonNull(color, "color");

        push();
        setColor(color);
        return this::pop;
    }

    /**
     * Создаёт скоуп, в котором базовый цвет заменяется на заданный
     * из покомпонентных значений.
     * <p>
     * Эквивалентен {@link #scoped(Vector4fc)}, но не требует создания
     * временного {@link Vector4f}.
     *
     * @param red   красный канал.
     * @param green зелёный канал.
     * @param blue  синий канал.
     * @param alpha альфа-канал.
     * @return скоуп; закрытие восстанавливает предыдущий базовый цвет.
     *
     * @see #scoped(Vector4fc)
     * @see ShaderColorScope
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public ShaderColorScope scoped(float red, float green, float blue, float alpha) {
        push();
        setColor(red, green, blue, alpha);
        return this::pop;
    }

    /**
     * Создаёт скоуп, в котором активируется заданная цветовая маска.
     * <p>
     * Эквивалентен {@code pushMask(mask)} с автоматическим {@code popMask()}
     * при закрытии скоупа. Удобен для затемнения группы объектов:
     * <pre>{@code
     * try (var ignored = color.scopedMask(new Vector4f(0.5f, 0.5f, 0.5f, 1f))) {
     *     drawDarkStuff();
     * }
     * }</pre>
     * <p>
     * <b>Вложенность.</b> Маски можно вкладывать — каждая последующая
     * умножается на предыдущую (см. {@link #pushMask(Vector4fc)}).
     * Скоуп маски не сбрасывает стек цвета, и наоборот.
     * <p>
     * <b>Безопасность исключений.</b> Если {@code mask} равен {@code null},
     * выбрасывается {@link NullPointerException} <i>до</i> сохранения
     * в стек — состояние {@link ShaderColor} остаётся неизменным.
     *
     * @param mask новая маска; не должна быть {@code null}.
     * @return скоуп; закрытие восстанавливает предыдущую маску.
     *
     * @throws NullPointerException если {@code mask == null}.
     *
     * @see #scopedMask(float, float, float, float)
     * @see #scoped(Vector4fc)
     * @see ShaderColorScope
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public ShaderColorScope scopedMask(Vector4fc mask) {
        pushMask(mask);
        return this::popMask;
    }

    /**
     * Создаёт скоуп, в котором активируется заданная цветовая маска
     * из покомпонентных значений.
     * <p>
     * Эквивалентен {@link #scopedMask(Vector4fc)}, но не требует создания
     * временного {@link Vector4f}.
     *
     * @param red   множитель красного канала.
     * @param green множитель зелёного канала.
     * @param blue  множитель синего канала.
     * @param alpha множитель альфа-канала.
     * @return скоуп; закрытие восстанавливает предыдущую маску.
     *
     * @see #scopedMask(Vector4fc)
     * @see ShaderColorScope
     */
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public ShaderColorScope scopedMask(float red, float green, float blue, float alpha) {
        pushMask(red, green, blue, alpha);
        return this::popMask;
    }

    private void updateColorWithMask() {
        this.colorWithMask.set(this.colorWithoutMask);
        if (!this.maskStack.isEmpty())
            mul(this.colorWithMask, this.maskStack.peek());
    }

    private void mul(Vector4f color, float[] array) {
        color.x *= array[0];
        color.y *= array[1];
        color.z *= array[2];
        color.w *= array[3];
    }

    private float[] createArray(Vector4fc color) {
        return new float[]{
                color.x(),
                color.y(),
                color.z(),
                color.w()
        };
    }
}
