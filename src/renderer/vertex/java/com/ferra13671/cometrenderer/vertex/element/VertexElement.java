package com.ferra13671.cometrenderer.vertex.element;

/*
 * Элемент вершинного формата.
 *  Используется для создания структуры вершины,
 *  которая будет сопоставляться с вершинами и их
 *  данными, поступающими через билдер.
 */
public class VertexElement {
    //Айди элемента в формате
    private final int id;
    //Количество данных в элементе
    private final int count;
    //Байтовый размер элемента
    private final int size;
    //Тип данных элемента
    private final VertexElementType<?> type;

    public VertexElement(int id, int count, VertexElementType<?> type) {
        this.id = id;
        this.count = count;
        this.size = this.count * type.byteSize();
        this.type = type;
    }

    /*
     * возвращает маску элемента
     */
    public int mask() {
        return 1 << this.id;
    }

    public VertexElementType<?> getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }
}
