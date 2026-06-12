package com.ferra13671.cometrenderer.vertex;

import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;

/**
 * Тип отрисовки вершин.
 *
 * @param glId айди типа отрисовки в OpenGL.
 * @param indexBufferGenerator генератор буффера индексов.
 */
@API(status = API.Status.MAINTAINED, since = "1.4")
public record DrawMode(int glId, IndexBufferGenerator indexBufferGenerator) {
	public static final DrawMode LINES = new DrawMode(GL11.GL_LINES, null);
	public static final DrawMode LINE_STRIP = new DrawMode(GL11.GL_LINE_STRIP, null);
	public static final DrawMode TRIANGLES = new DrawMode(GL11.GL_TRIANGLES, null);
	public static final DrawMode TRIANGLE_STRIP = new DrawMode(GL11.GL_TRIANGLE_STRIP, null);
	public static final DrawMode TRIANGLE_FAN = new DrawMode(GL11.GL_TRIANGLE_FAN, null);
	public static final DrawMode QUADS = new DrawMode(
			GL11.GL_TRIANGLES,
			new IndexBufferGenerator(4, 6, (indexList, firstVertexIndex) ->
				indexList.indexes(
						firstVertexIndex,
						firstVertexIndex + 1,
						firstVertexIndex + 2,
						firstVertexIndex + 2,
						firstVertexIndex + 3,
						firstVertexIndex
				)
			)
	);
}