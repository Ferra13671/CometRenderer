package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.device.directstate.ARBDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DefaultDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.device.state.PipelineStateManager;
import com.ferra13671.cometrenderer.device.state.PipelineStateManagerImpl;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.device.vertexformat.ARBVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.DefaultVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.VertexFormatManager;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;

@Setter
@Getter
@API(status = API.Status.INTERNAL, since = "3.0")
public class GLDevice {
    private final DirectStateManager directStateManager;
    private final VertexFormatManager vertexFormatManager;
    private final MeshBufferManager meshBufferManager;

    private final ResourceTracker resourceTracker = new ResourceTracker();

    private PipelineStateManager pipelineStateManager = new PipelineStateManagerImpl();

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

    public void blitFramebuffer(
            int srcFramebufferId, int srcWidth, int srcHeight,
            int dstFramebufferId, int dstWidth, int dstHeight,
            boolean copyDepth, boolean copyStencil
    ) {
        int mask = GL_COLOR_BUFFER_BIT;
        int filter = GL_LINEAR;

        if (copyDepth) {
            mask = mask | GL_DEPTH_BUFFER_BIT;
            filter = GL_NEAREST;
        }
        if (copyStencil) {
            mask = mask | GL_STENCIL_BUFFER_BIT;
            filter = GL_NEAREST;
        }

        getDirectStateManager().blitFramebuffer(
                srcFramebufferId, dstFramebufferId,
                0, 0, srcWidth, srcHeight,
                0, 0, dstWidth, dstHeight,
                mask, filter
        );
    }
}
