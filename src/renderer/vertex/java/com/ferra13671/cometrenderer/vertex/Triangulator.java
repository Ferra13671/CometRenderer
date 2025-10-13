package com.ferra13671.cometrenderer.vertex;

import it.unimi.dsi.fastutil.ints.IntConsumer;

public interface Triangulator {
	void accept(IntConsumer indexConsumer, int firstVertexIndex);
}