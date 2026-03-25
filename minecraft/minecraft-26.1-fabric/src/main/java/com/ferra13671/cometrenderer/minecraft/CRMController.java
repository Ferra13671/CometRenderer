package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.minecraft.mixins.IGpuDevice;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.VertexArrayCache;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import com.mojang.blaze3d.opengl.GlDevice;

import java.awt.*;

public class CRMController extends AbstractCRMController {
    @Getter
    private final VertexArrayCache vertexArrayCache;
    @Getter
    private final DirectStateAccess directStateAccess;
    private final ProjectionMatrixBuffer uiMatrix;
    private final Projection projection = new Projection();

    CRMController() {
        GlDevice device = ((GlDevice) ((IGpuDevice) RenderSystem.getDevice()).crm$$$getBackend());
        this.vertexArrayCache = device.vertexArrayCache();
        this.directStateAccess = device.directStateAccess();

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

        this.uiMatrix = new ProjectionMatrixBuffer("ui-matrix");
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
        this.projection.setupOrtho(
                -1000,
                1000,
                Minecraft.getInstance().getWindow().getWidth() / (float) scale,
                Minecraft.getInstance().getWindow().getHeight() / (float) scale,
                true
        );
        RenderSystem.setProjectionMatrix(
                this.uiMatrix.getBuffer(this.projection),
                ProjectionType.ORTHOGRAPHIC
        );
    }

    @Override
    protected void restoreUIMatrix() {
        RenderSystem.restoreProjectionMatrix();
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
        return new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), new Color(0, 0, 0, 0), 0);
    }
}
