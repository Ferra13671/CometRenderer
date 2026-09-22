package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.device.state.PipelineStateManagerImpl;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.minecraft.mixins.IGlCommandEncoder;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GLShaderLibraryBuilder;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.awt.*;

public class CRMController extends AbstractCRMController {
    private final CachedOrthoProjectionMatrixBuffer uiMatrix;

    CRMController() {
        CometRenderer.getDevice().setPipelineStateManager(new PipelineStateManagerImpl() {

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
            public void setDepthMask(boolean depthMask) {
                GlStateManager._depthMask(depthMask);
            }

            @Override
            public void setProgram(int programId) {
                CommandEncoder enc = RenderSystem.getDevice().createCommandEncoder();
                if (enc instanceof GlCommandEncoder)
                    ((IGlCommandEncoder) enc).crm$$$setLastProgram(null);
                GL20.glUseProgram(programId);
            }

            @Override
            public void setFramebuffer(int framebufferId) {
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);
            }

            @Override
            public void setReadFramebuffer(int framebufferId) {
                GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebufferId);
            }

            @Override
            public void setDrawFramebuffer(int framebufferId) {
                GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebufferId);
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

        this.uiMatrix = new CachedOrthoProjectionMatrixBuffer("ui-matrix", -1000, 1000, true);
    }

    @Override
    protected GLProgramSnippet loadMatrixSnippet() {
        return GLProgram.builder()
                .uniform("Projection", UniformType.BUFFER)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .buildSnippet();
    }

    @Override
    protected GLSLFileEntry getMatricesShaderLib() {
        return new GLShaderLibraryBuilder<>(CometLoader.STRING, getMatrixSnippet())
                .name("matrices")
                .library(
                        """
                        layout(std140) uniform Projection {
                            mat4 projMat;
                        };
                        uniform mat4 modelViewMat;
                        """
                )
                .build();
    }

    @Override
    protected void setupUIMatrix(int scale) {
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(
                uiMatrix.getBuffer(
                        Minecraft.getInstance().getWindow().getWidth() / (float) scale,
                        Minecraft.getInstance().getWindow().getHeight() / (float) scale
                ),
                ProjectionType.ORTHOGRAPHIC
        );
    }

    @Override
    protected void restoreUIMatrix() {
        RenderSystem.restoreProjectionMatrix();
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
                "Projection",
                UniformType.BUFFER,
                projectionUniform -> {
                    GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
                    projectionUniform.set(MinecraftBufferUniformUploaders.GPU_BUFFER_SLICE, slice);
                }
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
        return new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), new Color(0, 0, 0, 0), 0, 0);
    }

    @Override
    protected RenderColor getColorFromMinecraftCode(char code) {
        ChatFormatting formatting = ChatFormatting.getByCode(code);

        return formatting != null ? formatting.isColor() ? RenderColor.ofRGB(formatting.getColor()) : formatting == ChatFormatting.RESET ? RenderColor.WHITE : null : null;
    }
}
