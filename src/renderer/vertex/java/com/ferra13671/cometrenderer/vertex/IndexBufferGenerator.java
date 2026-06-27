package com.ferra13671.cometrenderer.vertex;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.BufferUsage;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.utils.index.IndexList;
import com.ferra13671.cometrenderer.utils.index.IndexType;
import com.ferra13671.cometrenderer.utils.MathUtils;
import com.ferra13671.cometrenderer.utils.Triangulator;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Объект, представляющий собой сборщик буффера индексов для определенного буффера вершин.
 * Данный сборщик позволяет разбивать при помощи индексов объекты на множество составляющих.
 *
 * @see Triangulator
 * @see IndexType
 * @see GpuBuffer
 */
@API(status = API.Status.MAINTAINED, since = "1.4")
public final class IndexBufferGenerator {
	/** Количество вершин до триангуляции. **/
	@Getter
	private final int vertexCountInShape;
	/** Количество вершин после триангуляции. **/
	@Getter
	private final int vertexCountInTriangulated;
	/** Триангулятор. **/
	private final Triangulator triangulator;
	/** Буффер индексов. **/
	private GpuBuffer indexBuffer;
	/** Тип индексов. **/
	@Getter
	private IndexType indexType = IndexType.SHORT;
	/** Прошлый требуемый размер. Необходимо для того, что бы если несколько раз отрисовывать одни и те же вершины, то не нужно было каждый раз создавать новый буффер индексов. **/
	private int size;

	public IndexBufferGenerator(int vertexCountInShape, int vertexCountInTriangulated, Triangulator triangulator) {
		this.vertexCountInShape = vertexCountInShape;
		this.vertexCountInTriangulated = vertexCountInTriangulated;
		this.triangulator = triangulator;
	}

	/**
	 * Возвращает количество индексов для данного количества вершин.
	 *
	 * @param vertices количество вершин, для которых нужно подсчитать количество индексов.
	 * @return количество индексов для данного количества вершин.
	 */
	public int getIndexCount(int vertices) {
		return vertices / this.vertexCountInShape * this.vertexCountInTriangulated;
	}

	/**
	 * Возвращает буффер вершин с требуемым количеством индексов.
	 *
	 * @param requiredSize требуемый размер.
	 * @return буффер вершин с требуемым количеством индексов.
	 */
	public GpuBuffer getIndexBuffer(int requiredSize) {
		if (requiredSize > this.size) {
			if (this.indexBuffer != null)
				this.indexBuffer.close();

			this.indexBuffer = generateIndexBuffer(requiredSize);
			this.size = requiredSize;
		}
		return this.indexBuffer;
	}

	private GpuBuffer generateIndexBuffer(int requiredSize) {
		requiredSize = MathUtils.roundUpToMultiple(requiredSize, this.vertexCountInTriangulated);

		int maxIndices = CometRenderer.getConfig().MAX_INDICES.getValue();
		if (requiredSize > maxIndices)
			ErrorHandlers.onIndexOverflow(maxIndices);

		this.indexType = IndexType.best(requiredSize);
		ByteBuffer buffer = MemoryUtil.memAlloc(requiredSize * indexType.bytes);

		int shapeCount = requiredSize / this.vertexCountInTriangulated;
		try {
			for (int i = 0; i < shapeCount; i++)
				this.triangulator.accept(
						new IndexList() {
							@Override
							public void index(int index) {
								indexType.putConsumer.accept(buffer, index);
							}

							@Override
							public void indexes(int... indexes) {
								for (int index : indexes)
									index(index);
							}
						},
						i * this.vertexCountInShape
				);

			buffer.flip();

			return new GpuBuffer(buffer, BufferUsage.STATIC_DRAW, BufferTarget.ELEMENT_ARRAY_BUFFER, true);
		} finally {
			MemoryUtil.memFree(buffer);
		}
	}
}