package com.ferra13671.cometrenderer.vertex;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

/**
 * Тип индексов, используемых в буффере индексов.
 *
 * @see IndexBufferGenerator
 */
@AllArgsConstructor
public enum IndexType {
	SHORT(
			2,
			GL11.GL_UNSIGNED_SHORT,
			(buffer, index) -> buffer.putShort(index.shortValue())
	),
	INT(
			4,
			GL11.GL_UNSIGNED_INT,
			(buffer, index) -> buffer.putInt(index.intValue())
	);

	/** Размер типа индесов в байтах. **/
	public final int bytes;
	/** Айди типа индексов в OpenGL. **/
	public final int glId;
	public final BiConsumer<ByteBuffer, Number> putConsumer;

	public static IndexType best(int indexCount) {
		return indexCount < Short.MAX_VALUE ? SHORT : INT;
	}
}