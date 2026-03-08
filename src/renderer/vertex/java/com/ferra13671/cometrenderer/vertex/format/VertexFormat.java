package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.NoSuchVertexElementException;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import lombok.Getter;
import org.apiguardian.api.API;

import java.io.Closeable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Формат вершины, представляющий собой структуру, используемую для привязки к буфферу вершин для последующей передачи в OpenGL и обработки рендерингом.
 * Если программа будет использоваться с буффером вершин, имеющим формат вершины, непредназначенным для данной программы, то последствия могут быть разными.
 *
 * @see VertexElement
 */
@API(status = API.Status.MAINTAINED, since = "1.4")
public class VertexFormat implements Closeable {
    public static final VertexFormat POSITION = VertexFormat.builder().build();
    public static final VertexFormat POSITION_COLOR = VertexFormat.builder()
            .element("Color", VertexElementType.FLOAT, 4)
            .build();
    public static final VertexFormat POSITION_TEXTURE = VertexFormat.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();
    public static final VertexFormat POSITION_COLOR_TEXTURE = POSITION_COLOR.toBuilder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();
    public static final VertexFormat POSITION_TEXTURE_COLOR = POSITION_TEXTURE.toBuilder()
            .element("Color", VertexElementType.FLOAT, 4)
            .build();

    /** Массив элементов формата вершины. **/
    @Getter
    @API(status = API.Status.INTERNAL)
    private final VertexElement[] vertexElements;
    /** Массив имен элементов формата вершины. **/
    private final String[] names;
    private final HashMap<String, VertexElement> elementsForNames = new HashMap<>();
    /** Маска формата вершины, используемая в билдере меша для проверки целостности вершины при окончании её сборки. **/
    @Getter
    @API(status = API.Status.INTERNAL)
    private final int elementsMask;
    /** Размер вершины в байтах. **/
    @Getter
    @API(status = API.Status.INTERNAL)
    private final int vertexSize;
    /** Оффсеты элементов формата вершины. **/
    @Getter
    @API(status = API.Status.INTERNAL)
    private final int[] elementOffsets;
    /** Буффер формата вершины, используемый для быстрой привязки аттрибутов буфферу вершин. **/
    private VertexFormatBuffer buffer = null;

    /**
     * @param vertexElements список элементов формата вершины.
     * @param elementNames список имен элементов формата вершины.
     */
    @API(status = API.Status.INTERNAL)
    public VertexFormat(List<VertexElement> vertexElements, List<String> elementNames) {
        this.vertexElements = vertexElements.toArray(new VertexElement[0]);
        this.names = elementNames.toArray(new String[0]);
        this.elementOffsets = new int[this.vertexElements.length];
        this.elementsMask = Arrays.stream(this.vertexElements).mapToInt(VertexElement::getMask).reduce(0, (a, b) -> a | b);

        int size = 0;
        int elementOffset = 0;
        for (int i = 0; i < this.vertexElements.length; i++) {
            VertexElement vertexElement = this.vertexElements[i];
            size += vertexElement.getSize();

            this.elementOffsets[i] = elementOffset;
            elementOffset += vertexElement.getSize();

            this.elementsForNames.put(this.names[i], vertexElement);
        }
        this.vertexSize = size;
    }

    @Override
    public void close() {
        GpuBuffer buffer = this.buffer.getBuffer();
        if (buffer != null)
            buffer.close();
    }

    /**
     * Возвращает Stream элементов вершины по остаточной маске.
     *
     * @param mask маска элементов вершины.
     * @return список элементов вершин, соответствующих маске.
     *
     * @see VertexElement
     */
    @API(status = API.Status.INTERNAL)
    public Stream<VertexElement> getElementsFromMask(int mask) {
        return Arrays.stream(this.vertexElements).filter(element -> element != null && (mask & element.getMask()) != 0);
    }

    /**
     * Возвращает элемент формата вершины по его имени.
     * Если в формате вершины элемента с данным именем не существует, то вызовется ошибка.
     *
     * @param name имя элемента формата вершины.
     * @return элемент формата вершины.
     *
     * @see VertexElement
     */
    @API(status = API.Status.MAINTAINED)
    public VertexElement getElement(String name) {
        VertexElement vertexElement = this.elementsForNames.get(name);
        if (vertexElement == null)
            CometRenderer.getExceptionManager().manageException(new NoSuchVertexElementException(name));
        return vertexElement;
    }

    /**
     * Возвращает имя элемента формата вершины.
     *
     * @param vertexElement элемент формата вершины.
     * @return имя элемента формата вершины.
     *
     * @see VertexElement
     */
    @API(status = API.Status.INTERNAL)
    public String getElementName(VertexElement vertexElement) {
        return this.names[vertexElement.getId()];
    }

    /**
     * Возвращает оффсет для данного элемента формата вершины.
     *
     * @param vertexElement элемент формата вершины.
     * @return оффсет для данного элемента.
     */
    @API(status = API.Status.INTERNAL)
    public int getElementOffset(VertexElement vertexElement) {
        return this.elementOffsets[vertexElement.getId()];
    }

    /**
     * Создаёт новый буффер формата вершины, если он ещё не был создан, и возвращает его.
     *
     * @param bufferSupplier метод, создающий новый буффер формата вершины.
     * @return буффер формата вершины.
     */
    @API(status = API.Status.INTERNAL)
    public VertexFormatBuffer getOrCreateBuffer(Supplier<VertexFormatBuffer> bufferSupplier) {
        if (this.buffer == null)
            this.buffer = bufferSupplier.get();

        return this.buffer;
    }

    @API(status = API.Status.EXPERIMENTAL, since = "2.7")
    public VertexFormatBuilder toBuilder() {
        VertexFormatBuilder builder = new VertexFormatBuilder();

        for (String name : this.names) {
            VertexElement element = this.elementsForNames.get(name);

            builder.element(name, element.getType(), element.getCount());
        }

        return builder;
    }

    /**
     * Возвращает новый сборщик формата вершины.
     *
     * @return новый сборщик формата вершины.
     */
    public static VertexFormatBuilder builder() {
        return new VertexFormatBuilder().element("Position", VertexElementType.FLOAT, 3);
    }
}
