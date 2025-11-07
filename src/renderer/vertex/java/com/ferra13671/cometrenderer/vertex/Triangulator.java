package com.ferra13671.cometrenderer.vertex;

import it.unimi.dsi.fastutil.ints.IntConsumer;

/**
 * Объект, обрабатывающий и разбивающий вершины на множество индексов.
 */
public interface Triangulator {

	void accept(IntConsumer indexConsumer, int firstVertexIndex);
}