package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.device.state.UncachedPipelineStateManager;
import com.ferra13671.cometrenderer.glsl.GLSLLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GLShaderLibraryBuilder;
import com.ferra13671.cometrenderer.stencil.StencilOpAction;
import com.ferra13671.cometrenderer.utils.*;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL33;

import java.util.Stack;

public class CRMController extends AbstractCRMController {
    private final Stack<Matrix4f> matrix4fStack = new Stack<>();

    CRMController() {
        CometRenderer.getDevice().setPipelineStateManager(new UncachedPipelineStateManager() {

            @Override
            public void setCullFace(boolean cullFace) {
                if (cullFace)
                    GlStateManager._enableCull();
                else
                    GlStateManager._disableCull();
            }

            @Override
            public void setBlend(boolean blend) {
                if (blend)
                    GlStateManager._enableBlend();
                else
                    GlStateManager._disableBlend();
            }

            @Override
            public void setBlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha) {
                GlStateManager._blendFuncSeparate(srcColor.glId, dstColor.glId, srcAlpha.glId, dstAlpha.glId);
            }

            @Override
            public void setScissor(boolean scissor) {
                if (scissor)
                    GlStateManager._enableScissorTest();
                else
                    GlStateManager._disableScissorTest();
            }

            @Override
            public void scissorBox(int x, int y, int width, int height) {
                GlStateManager._scissorBox(x, y, width, height);
            }

            @Override
            public void setStencilMask(boolean stencilMask) {
                if (stencilMask)
                    GlStateManager._stencilMask(0xFF);
                else
                    GlStateManager._stencilMask(0x00);
            }

            @Override
            public void setStencilFunc(CompareFunction function, int ref, int mask) {
                GlStateManager._stencilFunc(function.glId, ref, mask);
            }

            @Override
            public void setStencilOp(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed) {
                GlStateManager._stencilOp(
                        stencilFailed.glId,
                        stencilPassedDepthFailed.glId,
                        allPassed.glId
                );
            }

            @Override
            public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
                GlStateManager._colorMask(red, green, blue, alpha);
            }

            @Override
            public void setDepthTest(boolean depthTest) {
                if (depthTest)
                    GlStateManager._enableDepthTest();
                else
                    GlStateManager._disableDepthTest();
            }

            @Override
            public void setDepthFunc(CompareFunction func) {
                GlStateManager._depthFunc(func.glId);
            }

            @Override
            public void setDepthMask(boolean depthMask) {
                GlStateManager._depthMask(depthMask);
            }

            @Override
            public void setViewport(int x, int y, int width, int height) {
                GlStateManager._viewport(x, y, width, height);
            }

            @Override
            public void ensureTextureUnit(int unit) {
                GlStateManager._activeTexture(GL13.GL_TEXTURE0 + unit);
            }

            @Override
            public void bindTexture(int textureId) {
                GlStateManager._bindTexture(textureId);
            }

            @Override
            public void bindSampler(int samplerId) {
                bindSampler(GlStateManager._getActiveTexture(), samplerId);
            }

            @Override
            public void bindTexture(int unit, int textureId) {
                ensureTextureUnit(unit);
                GlStateManager._bindTexture(textureId);
            }

            @Override
            public void bindSampler(int unit, int samplerId) {
                ensureTextureUnit(unit);
                GL33.glBindSampler(unit, samplerId);
            }
        });
    }

    @Override
    protected GLProgramSnippet loadMatrixSnippet() {
        return GLProgram.builder()
                .uniform("projMat", UniformType.MATRIX4)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .buildSnippet();
    }

    @Override
    protected GLSLFileEntry getMatricesShaderLib() {
        return new GLShaderLibraryBuilder<>(GLSLLoader.STRING, getMatrixSnippet())
                .name("matrices")
                .library(
                        """
                        uniform mat4 projMat;
                        uniform mat4 modelViewMat;
                        """
                )
                .build();
    }

    @Override
    protected void setupUIMatrix(int scale) {
        this.matrix4fStack.push(RenderSystem.getProjectionMatrix());
        RenderSystem.setProjectionMatrix(
                (new Matrix4f()).setOrtho(
                        0.0F,
                        (float)((double) Minecraft.getInstance().getWindow().getWidth() / scale),
                        (float)((double)Minecraft.getInstance().getWindow().getHeight() / scale),
                        0.0F,
                        1000.0F,
                        21000.0F
                ),
                VertexSorting.ORTHOGRAPHIC_Z
        );
    }

    @Override
    protected void restoreUIMatrix() {
        RenderSystem.setProjectionMatrix(
                this.matrix4fStack.peek(),
                VertexSorting.ORTHOGRAPHIC_Z
        );
        this.matrix4fStack.pop();
    }

    @Override
    protected Vector2f getScaledMousePos(int scale) {
        return new Vector2f(
                (float) Minecraft.getInstance().mouseHandler.xpos(),
                (float) Minecraft.getInstance().mouseHandler.ypos()
        ).mul(1f / scale);
    }

    @Override
    protected void applyMatrixUniform() {
        CometRenderer.getCurrentProgram().consumeIfUniformPresent(
                "projMat",
                UniformType.MATRIX4,
                projectionUniform ->
                        projectionUniform.set(RenderSystem.getProjectionMatrix())
        );

        CometRenderer.getCurrentProgram().consumeIfUniformPresent(
                "modelViewMat",
                UniformType.MATRIX4,
                modelViewUniform ->
                        modelViewUniform.set(RenderSystem.getModelViewMatrix())
        );
    }

    @Override
    protected Framebuffer createMainFramebuffer() {
        return new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), RenderColor.TRANSLUCENT, 0, 0);
    }

    @Override
    protected RenderColor getColorFromMinecraftCode(char code) {
        ChatFormatting formatting = ChatFormatting.getByCode(code);

        return formatting != null ? formatting.isColor() ? RenderColor.ofRGB(formatting.getColor()) : formatting == ChatFormatting.RESET ? RenderColor.WHITE : null : null;
    }
}
