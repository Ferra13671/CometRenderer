package com.ferra13671.cometrenderer.vertex.format.instanced;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "3.1")
@Getter
@Setter
public class InstancedVertexFormatBuffer extends VertexFormatBuffer {
    private GpuBuffer instanceBuffer = null;

    public InstancedVertexFormatBuffer(int glId) {
        super(glId);
    }
}
