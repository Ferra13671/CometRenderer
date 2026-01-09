package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.NoSuchVertexElementException;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import lombok.Getter;

import java.io.Closeable;
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
public class VertexFormat implements Closeable {
    /** Карта элементов формата вершины по их имени. **/
    private final HashMap<String, VertexElement> vertexMap = new HashMap<>();
    /** Карта имен элементов структуры вершины. **/
    private final HashMap<VertexElement, String> namesMap = new HashMap<>();
    /** Список элементов формата вершины. **/
    @Getter
    private final List<VertexElement> vertexElements;
    /** Маска формата вершины, используемая в билдере меша для проверки целостности вершины при окончании её сборки. **/
    @Getter
    private final int elementsMask;
    /** Размер вершины в байтах. **/
    @Getter
    private final int vertexSize;
    /** Оффсеты элементов формата вершины. **/
    @Getter
    private final int[] elementOffsets;
    /** Буффер формата вершины, используемый для быстрой привязки аттрибутов буфферу вершин. **/
    private VertexFormatBuffer vertexFormatBuffer = null;

    /**
     * @param vertexElements список элементов формата вершины.
     * @param elementNames список имен элементов формата вершины.
     */
    public VertexFormat(List<VertexElement> vertexElements, List<String> elementNames) {
        this.vertexElements = vertexElements;
        this.elementOffsets = new int[this.vertexElements.size()];
        this.elementsMask = vertexElements.stream().mapToInt(VertexElement::mask).reduce(0, (a, b) -> a | b);

        int size = 0;
        int elementOffset = 0;
        //Проходимся по списку элементов
        for (int i = 0; i < vertexElements.size(); i++) {
            VertexElement vertexElement = vertexElements.get(i);
            //Добавляем к размеру формата размер элемента
            size += vertexElement.getSize();

            if (i > 0) {
                //Добавляем в массив оффсетов оффсет текущего элемента
                this.elementOffsets[i] = elementOffset;
            } else
                this.elementOffsets[i] = 0;

            elementOffset += vertexElement.getSize();

            //Добавляем в карту элементов элемент вместе с его именем
            this.vertexMap.put(elementNames.get(i), vertexElement);
            this.namesMap.put(vertexElement, elementNames.get(i));
        }
        //Присваиваем размеру формата полученный размер
        this.vertexSize = size;
    }

    @Override
    public void close() {
        GpuBuffer buffer = this.vertexFormatBuffer.buffer().get();
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
    public Stream<VertexElement> getElementsFromMask(int mask) {
        return this.vertexElements.stream().filter(element -> element != null && (mask & element.mask()) != 0);
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
    public VertexElement getVertexElement(String name) {
        VertexElement vertexElement = this.vertexMap.get(name);
        if (vertexElement == null)
            CometRenderer.manageException(new NoSuchVertexElementException(name));
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
    public String getVertexElementName(VertexElement vertexElement) {
        return this.namesMap.get(vertexElement);
    }

    /**
     * Возвращает оффсет для данного элемента формата вершины.
     *
     * @param vertexElement элемент формата вершины.
     * @return оффсет для данного элемента.
     */
    public int getElementOffset(VertexElement vertexElement) {
        return this.elementOffsets[vertexElement.getId()];
    }

    /**
     * Создаёт новый буффер формата вершины, если он ещё не был создан, и возвращает его.
     *
     * @param bufferSupplier метод, создающий новый буффер формата вершины.
     * @return буффер формата вершины.
     */
    public VertexFormatBuffer getVertexFormatBufferOrCreate(Supplier<VertexFormatBuffer> bufferSupplier) {
        if (this.vertexFormatBuffer == null)
            this.vertexFormatBuffer = bufferSupplier.get();

        return this.vertexFormatBuffer;
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
