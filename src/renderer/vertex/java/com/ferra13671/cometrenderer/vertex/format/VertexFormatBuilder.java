package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.VertexFormatOverflowException;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик вершинного формата.
 *
 * @see VertexFormat
 */
public final class VertexFormatBuilder extends Builder<VertexFormat> {
    /** Список элементов вершин. **/
    private final List<VertexElement> vertexElements = new ArrayList<>();
    /** Список имен элементов вершин. **/
    private final List<String> elementNames = new ArrayList<>();

    public VertexFormatBuilder() {
        super("vertex format");
    }

    /**
     * Добавляет элемент вершины в сборщика.
     *
     * @param name имя элемента вершины.
     * @param type тип элемента вершины.
     * @param count количество параметров в элементе.
     * @return сборщик вершинного формата.
     */
    public VertexFormatBuilder element(String name, VertexElementType<?> type, int count) {
        int id = this.vertexElements.size();
        this.elementNames.add(name);
        this.vertexElements.add(new VertexElement(id, count, type));

        int maxElements = CometRenderer.getRegistry().get(CometTags.MAX_VERTEX_ELEMENTS).orElseThrow().getValue();
        if (this.vertexElements.size() > maxElements)
            CometRenderer.getExceptionManager().manageException(new VertexFormatOverflowException(maxElements));

        return this;
    }

    @Override
    public VertexFormat build() {
        return new VertexFormat(this.vertexElements.toArray(new VertexElement[0]), this.elementNames);
    }
}
