package com.ferra13671.cometrenderer.vertex;

import org.lwjgl.opengl.GL11;

/**
 * Тип отрисовки вершин.
 */
//TODO возможность создавать свои типы отрисовки
public enum DrawMode {

	LINES(GL11.GL_LINES, ShapeIndexBuffer.sharedSequential),
	LINE_STRIP(GL11.GL_LINE_STRIP, ShapeIndexBuffer.sharedSequential),
	TRIANGLES(GL11.GL_TRIANGLES, ShapeIndexBuffer.sharedSequential),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP, ShapeIndexBuffer.sharedSequential),
	TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN, ShapeIndexBuffer.sharedSequential),
	QUADS(GL11.GL_TRIANGLES, ShapeIndexBuffer.sharedSequentialQuad);

	/** Айди типа отрисовки в OpenGL. **/
	public final int glId;
	/** Сборщик буффера индексов. **/
	public final ShapeIndexBuffer shapeIndexBuffer;

	/**
	 * @param glId айди типа отрисовки в OpenGL.
	 * @param shapeIndexBuffer сборщик буффера индексов.
	 */
	DrawMode(int glId, ShapeIndexBuffer shapeIndexBuffer) {
		this.glId = glId;
		this.shapeIndexBuffer = shapeIndexBuffer;
	}

	/**
	 * Возвращает количество индексов для текущего типа отрисовки и данного количества вершин.
	 *
	 * @param vertexCount количество вершин.
	 * @return количество индексов для данного количества вершин.
	 */
	public int getIndexCount(int vertexCount) {
		return switch (this) {
			case QUADS -> vertexCount / 4 * 6;
			case LINES, LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
		};
	}
}