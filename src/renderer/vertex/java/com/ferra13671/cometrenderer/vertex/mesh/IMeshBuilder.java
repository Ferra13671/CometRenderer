package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import org.apiguardian.api.API;
import org.joml.Matrix4f;

/**
 * Объект, собирающий вершины в цельный меш.
 *
 * @see IMesh
 */
@API(status = API.Status.MAINTAINED, since = "1.7")
public interface IMeshBuilder {

    IMesh build();

    IMesh buildNullable();

    /**
     * Создаёт новую вершину в сборщике меша.
     *
     * @param x координата вершины по X.
     * @param y координата вершины по Y.
     * @param z координата вершины по Z.
     * @return сборщик меша.
     */
    IMeshBuilder vertex(float x, float y, float z);

    /**
     * Создаёт новую вершину в сборщике меша.
     *
     * @param matrix4f матрица, обрабатывающая координаты вершины.
     * @param x координата вершины по X.
     * @param y координата вершины по Y.
     * @param z координата вершины по Z.
     * @return сборщик меша.
     */
    IMeshBuilder vertex(Matrix4f matrix4f, float x, float y, float z);

    /**
     * Добавляет элемент к последней созданной вершине в сборщике.
     *
     * @param name имя элемента.
     * @param elementType тип элемента.
     * @param values значения элемента.
     * @return сборщик меша.
     * @param <S> тип значений, передаваемых в элемент.
     */
    <S> IMeshBuilder element(String name, VertexElementType<S> elementType, S... values);
}
