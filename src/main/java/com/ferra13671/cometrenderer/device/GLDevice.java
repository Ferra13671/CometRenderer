package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.device.directstate.ARBDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DefaultDirectStateManager;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.device.state.PipelineStateManager;
import com.ferra13671.cometrenderer.device.state.PipelineStateManagerImpl;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.device.vertexformat.ARBVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.DefaultVertexFormatManager;
import com.ferra13671.cometrenderer.device.vertexformat.VertexFormatManager;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;

@Setter
@API(status = API.Status.INTERNAL, since = "3.0")
public class GLDevice {
    @Getter
    private final DirectStateManager directStateManager;
    @Getter
    private final VertexFormatManager vertexFormatManager;
    @Getter
    private final MeshBufferManager meshBufferManager;

    private final ResourceTracker resourceTracker = new ResourceTracker();

    @Getter
    private PipelineStateManager pipelineStateManager = new PipelineStateManagerImpl();

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

    public int createProgram() {
        int id = GL20.glCreateProgram();
        this.resourceTracker.registerProgram(id);

        return id;
    }

    public void deleteProgram(int id) {
        GL20.glDeleteProgram(id);
        this.resourceTracker.unregisterProgram(id);
    }

    public int createShader(ShaderType shaderType) {
        int id = GL20.glCreateShader(shaderType.glId);
        this.resourceTracker.registerShader(id);

        return id;
    }

    public void deleteShader(int id) {
        GL20.glDeleteShader(id);
        this.resourceTracker.unregisterShader(id);
    }

    public int createTexture() {
        int id = getDirectStateManager().createTexture();
        this.resourceTracker.registerTexture(id);

        return id;
    }

    public void deleteTexture(int id) {
        GL11.glDeleteTextures(id);
        this.resourceTracker.unregisterTexture(id);
    }

    public int createFramebuffer() {
        int id = getDirectStateManager().createFramebuffer();
        this.resourceTracker.registerFramebuffer(id);

        return id;
    }

    public void deleteFramebuffer(int id) {
        GL30.glDeleteFramebuffers(id);
        this.resourceTracker.unregisterFramebuffer(id);
    }

    public int createSampler() {
        int id = getDirectStateManager().createSampler();
        this.resourceTracker.registerSampler(id);

        return id;
    }

    public void deleteSampler(int id) {
        GL33.glDeleteSamplers(id);
        this.resourceTracker.unregisterSampler(id);
    }

    public int createVertexArray() {
        int id = getDirectStateManager().createVertexArray();
        this.resourceTracker.registerVertexArray(id);

        return id;
    }

    public void deleteVertexArray(int id) {
        GL30.glDeleteVertexArrays(id);
        this.resourceTracker.unregisterVertexArray(id);
    }

    public int createBuffer() {
        return getDirectStateManager().createBuffer();
    }

    public void deleteBuffer(int id) {
        GL15.glDeleteBuffers(id);
    }

    public int getProgramsCount() {
        return this.resourceTracker.getPrograms().size();
    }

    public int getShadersCount() {
        return this.resourceTracker.getShaders().size();
    }

    public int getTexturesCount() {
        return this.resourceTracker.getTextures().size();
    }

    public int getFramebuffersCount() {
        return this.resourceTracker.getFramebuffers().size();
    }

    public int getSamplersCount() {
        return this.resourceTracker.getSamplers().size();
    }

    public int getVertexArraysCount() {
        return this.resourceTracker.getVertexArrays().size();
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
