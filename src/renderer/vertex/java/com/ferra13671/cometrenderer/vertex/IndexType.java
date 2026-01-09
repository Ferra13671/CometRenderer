package com.ferra13671.cometrenderer.vertex;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

/**
 * Тип индексов, используемых в буффере индексов.
 *
 * @see IndexBufferGenerator
 */
@AllArgsConstructor
public enum IndexType {
	SHORT(2, GL11.GL_UNSIGNED_SHORT),
	INT(4, GL11.GL_UNSIGNED_INT);

	/** Размер типа индесов в байтах. **/
	public final int bytes;
	/** Айди типа индексов в OpenGL. **/
	public final int glId;

	/**
	 * Возвращает лучший по размерам тип индексов для числа.
	 *
	 * @param i число.
	 * @return лучший по размерам тип индексов.
	 */
	public static IndexType smallestFor(int i) {
		return (i & -65536) != 0 ? INT : SHORT;
	}
}