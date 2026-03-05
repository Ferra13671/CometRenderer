package com.ferra13671.cometrenderer.vertex;

import org.apiguardian.api.API;

import java.util.function.Consumer;

/**
 * Объект, обрабатывающий и разбивающий вершины на множество индексов.
 */
//TODO move to utils
@API(status = API.Status.MAINTAINED, since = "1.4")
public interface Triangulator {

	void accept(Consumer<Integer> indexConsumer, int firstVertexIndex);
}