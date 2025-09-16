package com.ferra13671.cometrenderer.vertex;

import org.lwjgl.opengl.GL11;

/*
 * Режим отрисовки вершин
 */
public enum DrawMode {
	LINES(GL11.GL_LINES),
	LINE_STRIP(GL11.GL_LINE_STRIP),
	TRIANGLES(GL11.GL_TRIANGLES),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
	TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
	QUADS(GL11.GL_TRIANGLES);

	public final int glId;

	DrawMode(int glId) {
		this.glId = glId;
	}

	public int getIndexCount(int vertexCount) {
		return switch (this) {
			case QUADS -> vertexCount / 4 * 6;
			case LINES, LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
		};
	}
}