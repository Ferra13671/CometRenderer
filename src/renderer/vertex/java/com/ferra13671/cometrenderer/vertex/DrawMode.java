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
	public static final DrawMode LINES = builder()
			.glId(GL11.GL_LINES)
			.build();
	public static final DrawMode LINE_STRIP = builder()
			.glId(GL11.GL_LINE_STRIP)
			.build();
	public static final DrawMode TRIANGLES = builder()
			.glId(GL11.GL_TRIANGLES)
			.build();
	public static final DrawMode TRIANGLE_STRIP = builder()
			.glId(GL11.GL_TRIANGLE_STRIP)
			.build();
	public static final DrawMode TRIANGLE_FAN = builder()
			.glId(GL11.GL_TRIANGLE_FAN)
			.build();
	public static final DrawMode QUADS = builder()
			.glId(GL11.GL_TRIANGLES)
			.useIndexBuffer(true)
			.indexBufferGenerator(
					new IndexBufferGenerator(4, 6, (indexConsumer, firstVertexIndex) -> {
						indexConsumer.accept(firstVertexIndex);
						indexConsumer.accept(firstVertexIndex + 1);
						indexConsumer.accept(firstVertexIndex + 2);
						indexConsumer.accept(firstVertexIndex + 2);
						indexConsumer.accept(firstVertexIndex + 3);
						indexConsumer.accept(firstVertexIndex);
					})
			)
			.indexCountFunction(
					vertices -> vertices / 4 * 6
			)
			.build();


	public static DrawModeBuilder builder() {
		return new DrawModeBuilder();
	}
}