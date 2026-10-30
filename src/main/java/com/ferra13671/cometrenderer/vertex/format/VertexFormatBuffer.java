package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apiguardian.api.API;

/**
 * Буффер вершинного формата.
 *
 * @see VertexFormat
 */
@Getter
@RequiredArgsConstructor
@API(status = API.Status.INTERNAL)
public class VertexFormatBuffer {
    /** Айди массива вершин. **/
    private final int glId;
    /** Буффер вершин. **/
    @Setter
    private GpuBuffer buffer = null;
}
