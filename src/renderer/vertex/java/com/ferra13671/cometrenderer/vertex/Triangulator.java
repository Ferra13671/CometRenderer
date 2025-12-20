package com.ferra13671.cometrenderer.vertex;

import java.util.function.Consumer;

/**
 * Объект, обрабатывающий и разбивающий вершины на множество индексов.
 */
public interface Triangulator {

	void accept(Consumer<Integer> indexConsumer, int firstVertexIndex);
}