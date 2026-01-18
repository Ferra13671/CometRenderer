package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Буффер вершинного формата.
 *
 * @see VertexFormat
 */
@Getter
@RequiredArgsConstructor
public final class VertexFormatBuffer {
    /** Айди массива вершин. **/
    private final int glId;
    /** Формат вершин. **/
    @NonNull
    private final VertexFormat vertexFormat;
    /** Буффер вершин. **/
    @Setter
    private GpuBuffer buffer = null;
}
