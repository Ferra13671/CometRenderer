package com.ferra13671.cometrenderer.device.state;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.ferra13671.cometrenderer.utils.stencil.StencilOpAction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apiguardian.api.API;
import org.lwjgl.opengl.*;

import java.util.stream.IntStream;

@API(status = API.Status.INTERNAL, since = "3.0")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PipelineStateManagerImpl implements PipelineStateManager {
    boolean blend = false;
    boolean scissor = false;
    boolean depthTest = false;
    boolean stencil = false;

    final boolean[] colorMask = {true, true, true, true};
    boolean depthMask = true;
    boolean stencilMask = true;

    SrcFactor srcColor = SrcFactor.ONE;
    DstFactor dstColor = DstFactor.ZERO;
    SrcFactor srcAlpha = SrcFactor.ONE;
    DstFactor dstAlpha = DstFactor.ZERO;

    final int[] scissorBox = {-1, -1, -1, -1};
    final int[] viewport = {-1, -1, -1, -1};

    AlphaFunction stencilFunc = AlphaFunction.ALWAYS;
    int stencilRef = 0;
    int stencilFuncMask = 0xFFFFFFFF;
    StencilOpAction stencilFailed = StencilOpAction.KEEP;
    StencilOpAction stencilPassedDepthFailed = StencilOpAction.KEEP;
    StencilOpAction allPassed = StencilOpAction.KEEP;

    int programId = 0;
    int readFramebufferId = 0;
    int drawFramebufferId = 0;

    int activeTextureUnit = 0;
    final int[] boundTextures = IntStream.generate(() -> -1).limit(32).toArray();
    final int[] boundSamplers = IntStream.generate(() -> -1).limit(32).toArray();

    @Override
    public void setBlend(boolean blend) {
        if (this.blend != blend) {
            if (blend)
                GL11.glEnable(GL11.GL_BLEND);
            else
                GL11.glDisable(GL11.GL_BLEND);

            this.blend = blend;
        }
    }

    @Override
    public void setBlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
        if (
                this.srcColor != srcColor ||
                this.dstColor != dstColor ||
                this.srcAlpha != srcAlpha ||
                this.dstAlpha != dstAlpha
        ) {
            GL14.glBlendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);

            this.srcColor = srcColor;
            this.dstColor = dstColor;
            this.srcAlpha = srcAlpha;
            this.dstAlpha = dstAlpha;
        }
    }

    @Override
    public void setScissor(boolean scissor) {
        if (this.scissor != scissor) {
            if (scissor)
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
            else
                GL11.glDisable(GL11.GL_SCISSOR_TEST);

            this.scissor = scissor;
        }
    }

    @Override
    public void scissorBox(int x, int y, int width, int height) {
        if (
                this.scissorBox[0] != x ||
                this.scissorBox[1] != y ||
                this.scissorBox[2] != width ||
                this.scissorBox[3] != height
        ) {
            GL11.glScissor(x, y, width, height);

            this.scissorBox[0] = x;
            this.scissorBox[1] = y;
            this.scissorBox[2] = width;
            this.scissorBox[3] = height;
        }
    }

    @Override
    public void setStencil(boolean stencil) {
        if (this.stencil != stencil) {
            if (stencil)
                GL11.glEnable(GL11.GL_STENCIL_TEST);
            else
                GL11.glDisable(GL11.GL_STENCIL_TEST);

            this.stencil = stencil;
        }
    }

    @Override
    public void setStencilMask(boolean stencilMask) {
        if (this.stencilMask != stencilMask) {
            if (stencilMask)
                GL11.glStencilMask(0xFF);
            else
                GL11.glStencilMask(0x00);

            this.stencilMask = stencilMask;
        }
    }

    @Override
    public void setStencilFunc(AlphaFunction function, int ref, int mask) {
        if (
                this.stencilFunc != function ||
                this.stencilRef != ref ||
                this.stencilFuncMask != mask
        ) {
            GL11.glStencilFunc(function.glId, ref, mask);

            this.stencilFunc = function;
            this.stencilRef = ref;
            this.stencilFuncMask = mask;
        }
    }

    @Override
    public void setStencilOp(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed) {
        if (
                this.stencilFailed != stencilFailed
                        || this.stencilPassedDepthFailed != stencilPassedDepthFailed
                        || this.allPassed != allPassed
        ) {
            GL11.glStencilOp(stencilFailed.glId, stencilPassedDepthFailed.glId, allPassed.glId);

            this.stencilFailed = stencilFailed;
            this.stencilPassedDepthFailed = stencilPassedDepthFailed;
            this.allPassed = allPassed;
        }
    }

    @Override
    public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (
                this.colorMask[0] != red ||
                this.colorMask[1] != green ||
                this.colorMask[2] != blue ||
                this.colorMask[3] != alpha
        ) {
            GL11.glColorMask(red, green, blue, alpha);

            this.colorMask[0] = red;
            this.colorMask[1] = green;
            this.colorMask[2] = blue;
            this.colorMask[3] = alpha;
        }
    }

    @Override
    public void setDepthTest(boolean depthTest) {
        if (this.depthTest != depthTest) {
            if (depthTest)
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            else
                GL11.glDisable(GL11.GL_DEPTH_TEST);

            this.depthTest = depthTest;
        }
    }

    @Override
    public void setDepthMask(boolean depthMask) {
        if (this.depthMask != depthMask) {
            GL11.glDepthMask(depthMask);

            this.depthMask = depthMask;
        }
    }

    @Override
    public void setProgram(int programId) {
        if (this.programId != programId) {
            GL20.glUseProgram(programId);

            this.programId = programId;
        }
    }

    @Override
    public void setFramebuffer(int framebufferId) {
        if (this.readFramebufferId != framebufferId || this.drawFramebufferId != framebufferId) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);

            this.readFramebufferId = framebufferId;
            this.drawFramebufferId = framebufferId;
        }
    }

    @Override
    public void setReadFramebuffer(int framebufferId) {
        if (this.readFramebufferId != framebufferId) {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebufferId);

            this.readFramebufferId = framebufferId;
        }
    }

    @Override
    public void setDrawFramebuffer(int framebufferId) {
        if (this.drawFramebufferId != framebufferId) {
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebufferId);

            this.drawFramebufferId = framebufferId;
        }
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        if (
                this.viewport[0] != x ||
                this.viewport[1] != y ||
                this.viewport[2] != width ||
                this.viewport[3] != height
        ) {
            GL11.glViewport(x, y, width, height);

            this.viewport[0] = x;
            this.viewport[1] = y;
            this.viewport[2] = width;
            this.viewport[3] = height;
        }
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
        bindTexture(this.activeTextureUnit, textureId);
    }

    @Override
    public void bindSampler(int samplerId) {
        bindSampler(this.activeTextureUnit, samplerId);
    }

    @Override
    public void bindTexture(int unit, int textureId) {
        if (this.boundTextures[unit] != textureId) {
            ensureTextureUnit(unit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

            this.boundTextures[unit] = textureId;
        }
    }

    @Override
    public void bindSampler(int unit, int samplerId) {
        if (this.boundSamplers[unit] != samplerId) {
            ensureTextureUnit(unit);
            GL33.glBindSampler(unit, samplerId);

            this.boundSamplers[unit] = samplerId;
        }
    }
}
