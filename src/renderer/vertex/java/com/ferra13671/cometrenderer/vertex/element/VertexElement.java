package com.ferra13671.cometrenderer.vertex.element;

/**
 * Объект, представляющий собой структурный элемент формата вершины.
 * Совокупность нескольких таких элементов формата вершины составляют целостный формат вершины.
 */
public class VertexElement {
    /** Айди элемента в формате вершины. **/
    private final int id;
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
        this.id = id;
        this.count = count;
        this.size = this.count * type.byteSize();
        this.type = type;
    }

    /**
     * Возвращает маску элемента.
     *
     * @return маска элемента.
     */
    public int mask() {
        return 1 << this.id;
    }

    /**
     * Возвращает айди элемента в формате вершины.
     *
     * @return айди элемента в формате вершины.
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает количество данных в элементе.
     *
     * @return количество данных в элементе.
     */
    public int getCount() {
        return count;
    }

    /**
     * Возвращает размер элемента в байтах
     *
     * @return размер элемента в байтах.
     */
    public int getSize() {
        return size;
    }

    /**
     * Возвращает тип данных элемента.
     *
     * @return тип данных элемента.
     */
    public VertexElementType<?> getType() {
        return type;
    }
}
