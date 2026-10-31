package com.ferra13671.cometrenderer.vertex.format.instanced;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

import java.io.Closeable;
import java.util.function.Supplier;

@API(status = API.Status.EXPERIMENTAL, since = "3.1")
@Getter
@Setter
public class InstancedVertexFormat implements Closeable {
    private final VertexFormat perVertexFormat;
    private final VertexFormat perInstanceFormat;

    private InstancedVertexFormatBuffer buffer = null;

    public InstancedVertexFormat(VertexFormat perVertexFormat, VertexFormat perInstanceFormat) {
        //TODO VertexFormat.hasElement
        if (perVertexFormat.getElement("Position") == null)
            ErrorHandlers.onBadVertexStructure("Position");

        this.perVertexFormat = perVertexFormat;
        this.perInstanceFormat = perInstanceFormat;
    }

    public InstancedVertexFormatBuffer getOrCreateBuffer(Supplier<InstancedVertexFormatBuffer> supplier) {
        if (this.buffer == null)
            this.buffer = supplier.get();

        return this.buffer;
    }

    @Override
    public void close() {
        if (this.buffer != null) {
            GpuBuffer buffer = this.buffer.getBuffer();
            if (buffer != null)
                buffer.close();

            GpuBuffer instanceBuffer = this.buffer.getInstanceBuffer();
            if (instanceBuffer != null)
                instanceBuffer.close();

            CometRenderer.getDevice().deleteVertexArray(this.buffer.getGlId());

            this.buffer = null;
        }
    }
}
