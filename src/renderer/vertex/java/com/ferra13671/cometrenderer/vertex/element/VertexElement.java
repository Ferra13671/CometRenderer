package com.ferra13671.cometrenderer.vertex.element;

import lombok.Getter;

/**
 * Объект, представляющий собой структурный элемент формата вершины.
 * Совокупность нескольких таких элементов формата вершины составляют целостный формат вершины.
 */
@Getter
public class VertexElement {
    /** Айди элемента в формате вершины. **/
    private final int id;
    /** Маска элемента. **/
    private final int mask;
    /** Количество данных в элементе. **/
    private final int count;
    /** Размер элемента в байтах. **/
    private final int size;
    /** Тип данных элемента. **/
    private final VertexElementType<?> type;

    /**
     * @param id айди элемента в формате вершины.
     * @param count количество данных в элементе.
     * @param type тип данных элемента.
     */
    public VertexElement(int id, int count, VertexElementType<?> type) {
        type.verify();

        this.id = id;
        this.mask = 1 << this.id;
        this.count = count * (type.size() / type.offset());
        this.size = this.count * type.size();
        this.type = type;
    }
}
