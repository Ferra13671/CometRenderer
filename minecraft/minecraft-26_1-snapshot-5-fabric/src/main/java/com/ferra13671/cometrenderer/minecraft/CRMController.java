package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.mojang.blaze3d.GraphicsWorkarounds;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlDebugLabel;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.VertexArrayCache;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.HashSet;

public class CRMController extends AbstractCRMController {
    @Getter
    private final VertexArrayCache vertexArrayCache;
    @Getter
    private final DirectStateAccess directStateAccess;
    private final CachedOrthoProjectionMatrixBuffer uiMatrix;

    CRMController() {
        HashSet<String> extensions = new HashSet<>(RenderSystem.getDevice().getEnabledExtensions());
        this.vertexArrayCache = VertexArrayCache.create(
                GL.getCapabilities(),
                GlDebugLabel.create(GL.getCapabilities(), false, extensions),
                extensions
        );
        this.directStateAccess = DirectStateAccess.create(
                GL.getCapabilities(),
                extensions,
                GraphicsWorkarounds.get(RenderSystem.getDevice())
        );

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

        this.uiMatrix = new CachedOrthoProjectionMatrixBuffer("CometRenderer UI matrix", -1000, 1000, true);
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
    protected void setupUIProjection(int scale) {
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
    protected void restoreUIProjection() {
        RenderSystem.restoreProjectionMatrix();
    }

    @Override
    protected void initMatrix() {
        CometRenderer.getGlobalProgram().consumeIfUniformPresent(
                "Projection",
                UniformType.BUFFER,
                projectionUniform -> {
                    GpuBufferSlice slice = RenderSystem.getProjectionMatrixBuffer();
                    projectionUniform.set(MinecraftBufferUniformUploaders.GPU_BUFFER_SLICE, slice);
                }
        );

        CometRenderer.getGlobalProgram().consumeIfUniformPresent(
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
