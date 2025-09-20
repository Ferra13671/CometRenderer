package com.ferra13671.cometrenderer.vertex;

import org.lwjgl.opengl.GL11;

/*
 * Режим отрисовки вершин
 */
public enum DrawMode {

	LINES(GL11.GL_LINES, ShapeIndexBuffer.sharedSequential),
	LINE_STRIP(GL11.GL_LINE_STRIP, ShapeIndexBuffer.sharedSequential),
	TRIANGLES(GL11.GL_TRIANGLES, ShapeIndexBuffer.sharedSequential),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP, ShapeIndexBuffer.sharedSequential),
	TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN, ShapeIndexBuffer.sharedSequential),
	QUADS(GL11.GL_TRIANGLES, ShapeIndexBuffer.sharedSequentialQuad);

	public final int glId;
	public final ShapeIndexBuffer shapeIndexBuffer;

	DrawMode(int glId, ShapeIndexBuffer shapeIndexBuffer) {
		this.glId = glId;
		this.shapeIndexBuffer = shapeIndexBuffer;
	}

	public int getIndexCount(int vertexCount) {
		return switch (this) {
			case QUADS -> vertexCount / 4 * 6;
			case LINES, LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
		};
	}
}