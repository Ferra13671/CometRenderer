package com.ferra13671.cometrenderer.vertex;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public final class ShapeIndexBuffer {
	private final int vertexCountInShape;
	private final int vertexCountInTriangulated;
	private final Triangulator triangulator;
	private GpuBuffer indexBuffer;
	private IndexType indexType = IndexType.SHORT;
	private int size;

	public ShapeIndexBuffer(int vertexCountInShape, int vertexCountInTriangulated, Triangulator triangulator) {
		this.vertexCountInShape = vertexCountInShape;
		this.vertexCountInTriangulated = vertexCountInTriangulated;
		this.triangulator = triangulator;
	}

	public boolean isLargeEnough(int requiredSize) {
		return requiredSize <= this.size;
	}

	public GpuBuffer getIndexBuffer(int requiredSize) {
		this.grow(requiredSize);
		return this.indexBuffer;
	}

	private void grow(int requiredSize) {
		if (!this.isLargeEnough(requiredSize)) {
			requiredSize = MathHelper.roundUpToMultiple(requiredSize * 2, this.vertexCountInTriangulated);
			int i = requiredSize / this.vertexCountInTriangulated;
			IndexType indexType = IndexType.smallestFor(
					i * this.vertexCountInShape
			);
			ByteBuffer byteBuffer = MemoryUtil.memAlloc(
					MathHelper.roundUpToMultiple(requiredSize * indexType.size, 4)
			);

			try {
				this.indexType = indexType;
				IntConsumer intConsumer = this.getIndexConsumer(byteBuffer);

				for (int l = 0; l < requiredSize; l += this.vertexCountInTriangulated)
					this.triangulator.accept(intConsumer, l * this.vertexCountInShape / this.vertexCountInTriangulated);

				byteBuffer.flip();
				if (this.indexBuffer != null)
					this.indexBuffer.close();

				this.indexBuffer = RenderSystem.getDevice().createBuffer(() -> "Auto Storage index buffer", 64, byteBuffer);
			} finally {
				MemoryUtil.memFree(byteBuffer);
			}

			this.size = requiredSize;
		}
	}

	private IntConsumer getIndexConsumer(ByteBuffer indexBuffer) {
		if (this.indexType == IndexType.SHORT)
			return index -> indexBuffer.putShort((short) index);
		else
			return indexBuffer::putInt;
	}

	public IndexType getIndexType() {
		return this.indexType;
	}

	public interface Triangulator {
		void accept(IntConsumer indexConsumer, int firstVertexIndex);
	}
}