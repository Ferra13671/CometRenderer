package com.ferra13671.cometrenderer.utils;

import org.apiguardian.api.API;

import java.util.function.Consumer;

/**
 * Объект, обрабатывающий и разбивающий вершины на множество индексов.
 */
@API(status = API.Status.MAINTAINED, since = "1.4")
public interface Triangulator {

	void accept(Consumer<Integer> indexConsumer, int firstVertexIndex);
}