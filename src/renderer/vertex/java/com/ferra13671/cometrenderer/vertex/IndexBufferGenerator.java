package com.ferra13671.cometrenderer.vertex;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.BufferUsage;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Объект, представляющий собой сборщик буффера индексов для определенного буффера вершин.
 * Данный сборщик позволяет разбивать при помощи индексов объекты на множество составляющих.
 * <p>
 * Примером может поступить сборщик индексов разбивающий вершины четырёхугольника на индексы двух треугольников:
 * <pre><code>
 *     //4 — количество вершин в 1 объекта(в данном случае у нас четырёхугольник, поэтому вершин у нас 4)
 *     //6 — количество индексов для 1 объекта(в данном случае мы разбиваем четырёхугольник на 2 треугольника, поэтому количество индексов будет 2 x 3 = 6)
 *     new ShapeIndexBuffer(4, 6, (indexConsumer, firstVertexIndex) -> {
 *      //Добавляем индексы для первого треугольника
 * 		indexConsumer.accept(firstVertexIndex);
 * 		indexConsumer.accept(firstVertexIndex + 1);
 * 		indexConsumer.accept(firstVertexIndex + 2);
 * 		//Добавляем индексы для второго треугольника
 * 		indexConsumer.accept(firstVertexIndex + 2);
 * 		indexConsumer.accept(firstVertexIndex + 3);
 * 		indexConsumer.accept(firstVertexIndex);
 * 	    })
 * </code></pre>
 *
 * @see Triangulator
 * @see IndexType
 * @see GpuBuffer
 */
public final class IndexBufferGenerator {

	/** Количество вершин до триангуляции. **/
	private final int vertexCountInShape;
	/** Количество вершин после триангуляции. **/
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
	 * Возвращает буффер вершин с требуемым количеством индексов.
	 *
	 * @param requiredSize требуемый размер.
	 * @param standalone должен ли быть буффер вершин автономным или нет. Если буффер индексов будет являться автономным, то он будет храниться в памяти до того времени, пока вы сами его не удалите.
	 * @return буффер вершин с требуемым количеством индексов.
	 */
	public GpuBuffer getIndexBuffer(int requiredSize, boolean standalone) {
		if (standalone)
			return generateIndexBuffer(requiredSize);
		else {
			if (requiredSize > this.size) {
				if (this.indexBuffer != null)
					this.indexBuffer.close();

				this.indexBuffer = generateIndexBuffer(requiredSize);
				this.size = requiredSize;
			}
			return this.indexBuffer;
		}
	}

	private GpuBuffer generateIndexBuffer(int requiredSize) {
		requiredSize = roundUpToMultiple(requiredSize, this.vertexCountInTriangulated);

		this.indexType = IndexType.best(requiredSize);
		ByteBuffer buffer = MemoryUtil.memAlloc(requiredSize * indexType.bytes);

		int shapeCount = requiredSize / this.vertexCountInTriangulated;
		try {
			for (int i = 0; i < shapeCount; i++)
				this.triangulator.accept(
						index -> this.indexType.putConsumer.accept(buffer, index),
						i * this.vertexCountInShape
				);

			buffer.flip();

			return new GpuBuffer(buffer, BufferUsage.STATIC_DRAW, BufferTarget.ELEMENT_ARRAY_BUFFER);
		} finally {
			MemoryUtil.memFree(buffer);
		}
	}

	private int roundUpToMultiple(int value, int divisor) {
		return ceilDiv(value, divisor) * divisor;
	}

	private int ceilDiv(int a, int b) {
		return -Math.floorDiv(-a, b);
	}
}