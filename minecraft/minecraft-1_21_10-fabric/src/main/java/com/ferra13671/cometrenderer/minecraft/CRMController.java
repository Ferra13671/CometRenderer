package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.minecraft.mixins.IGlCommandEncoder;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
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
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.*;

public class CRMController extends AbstractCRMController {
    private final CachedOrthoProjectionMatrixBuffer uiMatrix;

    CRMController() {
        State.BLEND = new State.BooleanState() {
            @Override
            public void enable() {
                GlStateManager._enableBlend();
            }

            @Override
            public void disable() {
                GlStateManager._disableBlend();
            }
        };
        State.SCISSOR = new State.BooleanState() {
            @Override
            public void enable() {
                GlStateManager._enableScissorTest();
            }

            @Override
            public void disable() {
                GlStateManager._disableScissorTest();
            }
        };
        State.TEXTURE = new State.TextureState() {
            @Override
            public void activeTexture(int activeTexture) {
                GlStateManager._activeTexture(activeTexture);
            }

            @Override
            public void bindTexture(int texture) {
                GlStateManager._bindTexture(texture);
            }
        };
        State.FRAMEBUFFER = (id, viewport, width, height) -> {
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
            if (viewport)
                GlStateManager._viewport(0,0, width, height);
        };
        State.PROGRAM = id -> {
            CommandEncoder enc = RenderSystem.getDevice().createCommandEncoder();
            if (enc instanceof GlCommandEncoder)
                ((IGlCommandEncoder) enc).crm$$$setLastProgram(null);
            GL20.glUseProgram(id);
        };

        this.uiMatrix = new CachedOrthoProjectionMatrixBuffer("ui-matrix", -1000, 1000, true);
    }

    @Override
    protected GlProgramSnippet loadMatrixSnippet() {
        return CometLoaders.IN_JAR.createProgramBuilder()
                .uniform("Projection", UniformType.BUFFER)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .buildSnippet();
    }

    @Override
    protected GlslFileEntry getMatricesShaderLib() {
        return new GlShaderLibraryBuilder<>(CometLoaders.STRING, getMatrixSnippet())
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
