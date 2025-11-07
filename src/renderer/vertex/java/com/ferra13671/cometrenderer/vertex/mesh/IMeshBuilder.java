package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import org.joml.Matrix4f;

/**
 * Объект, собирающий вершины в цельный меш.
 *
 * @param <T> класс сборщика, который будет расширять данный интерфейс.
 * @param <G> класс, расширяющий интерфейс меша.
 *
 * @see IMesh
 */
public interface IMeshBuilder<T, G extends IMesh> {

    /**
     * Собирает все добавленные вершины в цельный меш.
     * Если не удалось собрать меш, то вернется null.
     *
     * @return готовый меш либо null, если собрать его не удалось.
     *
     * @see IMesh
     */
    G buildNullable();

    /**
     * Собирает все добавленные вершины в цельный меш.
     * Если не удалось собрать меш, то вызовется ошибка.
     *
     * @return готовый меш.
     *
     * @see IMesh
     */
    G buildOrThrow();

    /**
     * Создаёт новую вершину в сборщике меша.
     *
     * @param x координата вершины по X.
     * @param y координата вершины по Y.
     * @param z координата вершины по Z.
     * @return сборщик меша.
     */
    T vertex(float x, float y, float z);

    /**
     * Создаёт новую вершину в сборщике меша.
     *
     * @param matrix4f матрица, обрабатывающая координаты вершины.
     * @param x координата вершины по X.
     * @param y координата вершины по Y.
     * @param z координата вершины по Z.
     * @return сборщик меша.
     */
    T vertex(Matrix4f matrix4f, float x, float y, float z);

    /**
     * Добавляет элемент к последней созданной вершине в сборщике.
     *
     * @param name имя элемента.
     * @param elementType тип элемента.
     * @param values значения элемента.
     * @return сборщик меша.
     * @param <S> тип значений, передаваемых в элемент.
     */
    <S> T element(String name, VertexElementType<S> elementType, S... values);
}
