package com.ferra13671.cometrenderer.device.state;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.utils.stencil.StencilOpAction;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
public interface PipelineStateManager {

    void setBlend(boolean blend);

    void setBlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha);

    void setScissor(boolean scissor);

    void scissorBox(int x, int y, int width, int height);

    void setStencil(boolean stencil);

    void setStencilMask(boolean stencilMask);

    void setStencilFunc(AlphaFunction function, int ref, int mask);

    void setStencilOp(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed);

    void setColorMask(boolean red, boolean green, boolean blue, boolean alpha);

    void setDepthTest(boolean depthTest);

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
