package com.ferra13671.cometrenderer.device.state;

import com.ferra13671.cometrenderer.utils.CompareFunction;
import com.ferra13671.cometrenderer.utils.Face;
import com.ferra13671.cometrenderer.utils.FrontFace;
import com.ferra13671.cometrenderer.utils.PolygonMode;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.stencil.StencilOpAction;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
public interface PipelineStateManager {

    @API(status = API.Status.INTERNAL, since = "3.1")
    void setCullFace(boolean cullFace);

    @API(status = API.Status.INTERNAL, since = "3.1")
    void setCullFaceMode(Face face);

    @API(status = API.Status.INTERNAL, since = "3.1")
    void setFrontFace(FrontFace frontFace);

    @API(status = API.Status.INTERNAL, since = "3.1")
    void setPolygonMode(PolygonMode polygonMode);

    void setBlend(boolean blend);

    void setBlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha);

    void setScissor(boolean scissor);

    void scissorBox(int x, int y, int width, int height);

    void setStencil(boolean stencil);

    void setStencilMask(boolean stencilMask);

    void setStencilFunc(CompareFunction function, int ref, int mask);

    void setStencilOp(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed);

    void setColorMask(boolean red, boolean green, boolean blue, boolean alpha);

    void setDepthTest(boolean depthTest);

    @API(status = API.Status.INTERNAL, since = "3.1")
    void setDepthFunc(CompareFunction func);

    void setDepthMask(boolean depthMask);

    void setProgram(int programId);

    void setFramebuffer(int framebufferId);

    void setReadFramebuffer(int framebufferId);

    void setDrawFramebuffer(int framebufferId);

    void setViewport(int x, int y, int width, int height);

    void ensureTextureUnit(int unit);

    void bindTexture(int textureId);

    void bindSampler(int samplerId);

    void bindTexture(int unit, int textureId);

    void bindSampler(int unit, int samplerId);
}
