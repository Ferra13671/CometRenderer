package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.NoSuchVertexElementException;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/*
 * Формат вершин, используемый для привязки к буфферу вершин и последующей передаче буффера в OpenGL для переваривания рендеринга.
 * Если програма предназначена для формата вершин, несовместимым с выбранным в билдере вершин, то последствия могут быть разными.
 */
public class VertexFormat {
    //Карта элементов структуры вершин по их имени
    private final HashMap<String, VertexElement> vertexMap = new HashMap<>();
    //Карта имен элементов структуры вершин
    private final HashMap<VertexElement, String> namesMap = new HashMap<>();
    private final List<VertexElement> vertexElements;
    //Маска формата вершины, используемая в билдере вершин для проверки построенной вершины на правильность её структуры.
    private final int elementsMask;
    //Размер вершины в байтах.
    private final int vertexSize;
    //Оффсеты элементов структуры вершин
    private final int[] elementOffsets;

    public VertexFormat(List<VertexElement> vertexElements, List<String> elementNames) {
        this.vertexElements = vertexElements;
        elementOffsets = new int[this.vertexElements.size()];
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
                elementOffsets[i] = elementOffset;
            } else
                elementOffsets[i] = 0;

            elementOffset += vertexElement.getSize();

            //Добавляем в карту элементов элемент вместе с его именем
            vertexMap.put(elementNames.get(i), vertexElement);
            namesMap.put(vertexElement, elementNames.get(i));
        }
        //Присваиваем размеру формата полученный размер
        this.vertexSize = size;
    }

    public int getElementsMask() {
        return elementsMask;
    }

    public Stream<VertexElement> getElementsFromMask(int mask) {
        return vertexElements.stream().filter(element -> element != null && (mask & element.mask()) != 0);
    }

    /*
     * Возвращает список элементов формата
     */
    public List<VertexElement> getVertexElements() {
        return vertexElements;
    }

    /*
     * Возвращает элемент структуры вершины по его имени
     */
    public VertexElement getVertexElement(String name) {
        VertexElement vertexElement = vertexMap.get(name);
        if (vertexElement == null)
            ExceptionPrinter.printAndExit(new NoSuchVertexElementException(name));
        return vertexElement;
    }

    /*
     * Возвращает имя элемента структуры вершины
     */
    public String getVertexElementName(VertexElement vertexElement) {
        return namesMap.get(vertexElement);
    }

    /*
     * Возвращает размер вершины
     */
    public int getVertexSize() {
        return vertexSize;
    }

    public int[] getElementOffsets() {
        return elementOffsets;
    }

    /*
     * Возвращает оффсет для данного элемента формата
     */
    public int getElementOffset(VertexElement vertexElement) {
        return elementOffsets[vertexElement.getId()];
    }
}
