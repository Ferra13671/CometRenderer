package com.ferra13671.cometrenderer.device.vertexformat;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormat;

/**
 * Менеджер установки формата вершины к буфферу вершин.
 */
public interface VertexFormatManager {

    /**
     * Устанавливает формат вершины к буфферу вершин.
     *
     * @param vertexBuffer буффер вершин.
     * @param vertexFormat формат вершины.
     */
    void bindFormatBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat);

    /**
     * Настраивает VAO для instanced rendering: вершинные атрибуты (divisor=0)
     * из {@code vertexBuffer}, инстансные (divisor=1) из {@code instanceBuffer}.
     * <p>
     * Instance-атрибуты должны начинаться с location {@code vertexFormat.getVertexElements().length}.
     * Матрицы (Matrix3fc/Matrix4fc) автоматически разбиваются на 3/4 consecutive vec-locations.
     *
     * @param vertexBuffer  буфер вершин (binding 0).
     * @param instancedVertexFormat  формат вершин с per-instance элементами.
     * @param instanceBuffer буфер per-instance данных (binding 1).
     */
    void bindInstancedFormatBuffer(GpuBuffer vertexBuffer, GpuBuffer instanceBuffer, InstancedVertexFormat instancedVertexFormat);
}
