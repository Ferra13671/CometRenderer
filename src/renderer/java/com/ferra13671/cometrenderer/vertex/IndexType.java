package com.ferra13671.cometrenderer.vertex;

import org.lwjgl.opengl.GL11;

/*
 * Тип индексов
 */
public enum IndexType {
	SHORT(2, GL11.GL_UNSIGNED_SHORT),
	INT(4, GL11.GL_UNSIGNED_INT);

	public final int size;
	public final int glId;

	IndexType(int size, int glId) {
		this.size = size;
		this.glId = glId;
	}

	public static IndexType smallestFor(int i) {
		return (i & -65536) != 0 ? INT : SHORT;
	}
}