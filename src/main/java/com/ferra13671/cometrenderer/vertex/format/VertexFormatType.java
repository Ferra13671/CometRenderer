package com.ferra13671.cometrenderer.vertex.format;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
public enum VertexFormatType {
    PerVertex,
    @API(status = API.Status.EXPERIMENTAL, since = "3.1")
    PerInstance
}
