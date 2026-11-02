package com.ferra13671.cometrenderer.device.state;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.stencil.StencilOpAction;
import com.ferra13671.cometrenderer.utils.*;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import org.apiguardian.api.API;
import org.lwjgl.opengl.*;

@API(status = API.Status.INTERNAL, since = "3.1")
public class UncachedPipelineStateManager implements PipelineStateManager {

    private int activeTextureUnit = 0;

    @Override
    public void bindBuffer(BufferTarget target, int bufferId) {
        GL15.glBindBuffer(target.glId, bufferId);
    }

    @Override
    public void bindVAO(int vaoId) {
        GL30.glBindVertexArray(vaoId);
    }

    @Override
    public void setCullFace(boolean cullFace) {
        if (cullFace)
            GL11.glEnable(GL11.GL_CULL_FACE);
        else
            GL11.glDisable(GL11.GL_CULL_FACE);
    }

    @Override
    public void setCullFaceMode(Face face) {
        GL11.glCullFace(face.glId);
    }

    @Override
    public void setFrontFace(FrontFace frontFace) {
        GL11.glFrontFace(frontFace.glId);
    }

    @Override
    public void setPolygonMode(PolygonMode polygonMode) {
        GL11.glPolygonMode(Face.FrontAndBack.glId, polygonMode.glId);
    }

    @Override
    public void setBlend(boolean blend) {
        if (blend)
            GL11.glEnable(GL11.GL_BLEND);
        else
            GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void setBlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        GL14.glBlendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);
    }

    @Override
    public void setBlendEquation(BlendEquation color, BlendEquation alpha) {
        GL20.glBlendEquationSeparate(color.glId, alpha.glId);
    }

    @Override
    public void setScissor(boolean scissor) {
        if (scissor)
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        else
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void scissorBox(int x, int y, int width, int height) {
        GL11.glScissor(x, y, width, height);
    }

    @Override
    public void setStencil(boolean stencil) {
        if (stencil)
            GL11.glEnable(GL11.GL_STENCIL_TEST);
        else
            GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    @Override
    public void setStencilMask(boolean stencilMask) {
        if (stencilMask)
            GL11.glStencilMask(0xFF);
        else
            GL11.glStencilMask(0x00);
    }

    @Override
    public void setStencilFunc(CompareFunction function, int ref, int mask) {
        GL11.glStencilFunc(function.glId, ref, mask);
    }

    @Override
    public void setStencilOp(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed) {
        GL11.glStencilOp(stencilFailed.glId, stencilPassedDepthFailed.glId, allPassed.glId);
    }

    @Override
    public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glColorMask(red, green, blue, alpha);
    }

    @Override
    public void setDepthTest(boolean depthTest) {
        if (depthTest)
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        else
            GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void setDepthFunc(CompareFunction func) {
        GL11.glDepthFunc(func.glId);
    }

    @Override
    public void setDepthMask(boolean depthMask) {
        GL11.glDepthMask(depthMask);
    }

    @Override
    public void setProgram(int programId) {
        GL20.glUseProgram(programId);
    }

    @Override
    public void setFramebuffer(int framebufferId) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);
    }

    @Override
    public void setReadFramebuffer(int framebufferId) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebufferId);
    }

    @Override
    public void setDrawFramebuffer(int framebufferId) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebufferId);
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    @Override
    public void ensureTextureUnit(int unit) {
        if (this.activeTextureUnit != unit) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
            this.activeTextureUnit = unit;
        }
    }

    @Override
    public void bindTexture(int textureId) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    @Override
    public void bindSampler(int samplerId) {
        bindSampler(this.activeTextureUnit, samplerId);
    }

    @Override
    public void bindTexture(int unit, int textureId) {
        ensureTextureUnit(unit);
        bindTexture(textureId);
    }

    @Override
    public void bindSampler(int unit, int samplerId) {
        ensureTextureUnit(unit);
        GL33.glBindSampler(unit, samplerId);
    }
}
