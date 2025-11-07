package com.ferra13671.cometrenderer.vertex;

import org.lwjgl.opengl.GL11;

import java.util.function.Function;

/**
 * Тип отрисовки вершин.
 *
 * @param glId айди типа отрисовки в OpenGL.
 * @param useIndexBuffer использует ли тип отрисовки буффер индексов или нет.
 * @param indexBufferGenerator генератор буффера индексов.
 * @param indexCountFunction функция, возвращающая количество индексов для данного количества вершин.
 */
public record DrawMode(int glId, boolean useIndexBuffer, IndexBufferGenerator indexBufferGenerator, Function<Integer, Integer> indexCountFunction) {
	public static final DrawMode LINES = new DrawMode(
			GL11.GL_LINES,
			false,
			null,
			vertices -> 0
	);
	public static final DrawMode LINE_STRIP = new DrawMode(
			GL11.GL_LINE_STRIP,
			false,
			null,
			vertices -> 0
	);
	public static final DrawMode TRIANGLES = new DrawMode(
			GL11.GL_TRIANGLES,
			false,
			null,
			vertices -> 0
	);
	public static final DrawMode TRIANGLE_STRIP = new DrawMode(
			GL11.GL_TRIANGLE_STRIP,
			false,
			null,
			vertices -> 0
	);
	public static final DrawMode TRIANGLE_FAN = new DrawMode(
			GL11.GL_TRIANGLE_FAN,
			false,
			null,
			vertices -> 0
	);
	public static final DrawMode QUADS = new DrawMode(
			GL11.GL_TRIANGLES,
			true,
			new IndexBufferGenerator(4, 6, (indexConsumer, firstVertexIndex) -> {
				indexConsumer.accept(firstVertexIndex);
				indexConsumer.accept(firstVertexIndex + 1);
				indexConsumer.accept(firstVertexIndex + 2);
				indexConsumer.accept(firstVertexIndex + 2);
				indexConsumer.accept(firstVertexIndex + 3);
				indexConsumer.accept(firstVertexIndex);
			}),
			vertices -> vertices / 4 * 6
	);
}