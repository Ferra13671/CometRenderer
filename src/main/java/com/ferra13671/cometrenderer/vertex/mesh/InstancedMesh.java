package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatType;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormat;
import org.apiguardian.api.API;

import java.util.Objects;

@API(status = API.Status.EXPERIMENTAL, since = "3.1")
public record InstancedMesh(
        IMesh perVertexMesh,
        IMesh perInstanceMesh,
        InstancedVertexFormat vertexFormat,
        int instanceCount,
        boolean closePerVertexMesh,
        boolean closePerInstanceMesh
) implements AutoCloseable {

    public InstancedMesh(IMesh perVertexMesh, IMesh perInstanceMesh, InstancedVertexFormat vertexFormat, int instanceCount) {
        this(perVertexMesh, perInstanceMesh, vertexFormat, instanceCount, true, true);
    }

    public InstancedMesh {
        Objects.requireNonNull(perVertexMesh, "perVertexMesh");
        Objects.requireNonNull(perInstanceMesh, "perInstanceMesh");
        Objects.requireNonNull(vertexFormat, "vertexFormat");

        if (perVertexMesh.getVertexFormat().getType() != VertexFormatType.PerVertex)
            ErrorHandlers.onWrongVertexFormatType(perVertexMesh.getVertexFormat().getType(), VertexFormatType.PerVertex);
        if (perInstanceMesh.getVertexFormat().getType() != VertexFormatType.PerInstance)
            ErrorHandlers.onWrongVertexFormatType(perInstanceMesh.getVertexFormat().getType(), VertexFormatType.PerInstance);

        if (vertexFormat.getPerVertexFormat() != perVertexMesh.getVertexFormat())
            ErrorHandlers.onMeshStructureException("vertexFormat.getPerVertexFormat() must match perVertexMesh.getVertexFormat()");
        if (vertexFormat.getPerInstanceFormat() != perInstanceMesh.getVertexFormat())
            ErrorHandlers.onMeshStructureException("vertexFormat.getPerInstanceFormat() must match perInstanceMesh.getVertexFormat()");

        int available = perInstanceMesh.getVertexCount();
        if (instanceCount < 0 || instanceCount > available)
            ErrorHandlers.onMeshStructureException(String.format("instanceCount(%d) must be in [0, %d]", instanceCount, available));
    }

    @Override
    public void close() {
        if (this.closePerVertexMesh)
            this.perVertexMesh.close();
        if (this.closePerInstanceMesh)
            this.perInstanceMesh.close();
    }
}
