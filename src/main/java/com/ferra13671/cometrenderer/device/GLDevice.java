package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.device.directstate.ARBDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DefaultDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.device.vertexformat.ARBVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.DefaultVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.VertexFormatManager;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

@Setter
@API(status = API.Status.MAINTAINED, since = "2.9")
public class GLDevice {
    @Getter
    private final DirectStateManager directStateManager;
    @Getter
    private final VertexFormatManager vertexFormatManager;
    @Getter
    private final MeshBufferManager meshBufferManager;

    @API(status = API.Status.MAINTAINED, since = "3.0")
    @Getter
    private GLProgram currentProgram;

    public GLDevice() {
        this.directStateManager = GLCapabilities.supportsDirectStateAccess() ?
                new ARBDirectStateManager()
                :
                new DefaultDirectStateManager();

        this.vertexFormatManager = GLCapabilities.supportsVertexAttributeBindings() ?
                new ARBVertexFormatManager()
                :
                new DefaultVertexFormatManager();

        this.meshBufferManager = GLCapabilities.supportsBufferStorage() ?
                MeshBufferManager.ARB
                :
                MeshBufferManager.DEFAULT;
    }
}
