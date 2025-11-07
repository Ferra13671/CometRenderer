package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик вершинного формата.
 *
 * @see VertexFormat
 */
public final class VertexFormatBuilder {
    /** Список элементов вершин. **/
    private final List<VertexElement> vertexElements = new ArrayList<>();
    /** Список имен элементов вершин. **/
    private final List<String> elementNames = new ArrayList<>();

    public VertexFormatBuilder() {}

    /**
     * Добавляет элемент вершины в сборщика.
     *
     * @param name имя элемента вершины.
     * @param type тип элемента вершины.
     * @param count количество параметров в элементе.
     * @return сборщик вершинного формата.
     */
    public VertexFormatBuilder element(String name, VertexElementType<?> type, int count) {
        int id = vertexElements.size();
        elementNames.add(name);
        vertexElements.add(new VertexElement(id, count, type));

        return this;
    }

    /**
     * Собирает элементы вершины в формат вершины.
     *
     * @return формат вершины.
     *
     * @see VertexFormat
     */
    public VertexFormat build() {
        return new VertexFormat(vertexElements, elementNames);
    }
}
