package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;

import java.util.Stack;

public class CRMController extends AbstractCRMController {
    private final Stack<Matrix4f> matrix4fStack = new Stack<>();

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
    }

    @Override
    protected GlProgramSnippet loadMatrixSnippet() {
        return CometLoaders.IN_JAR.createProgramBuilder()
                .uniform("projMat", UniformType.MATRIX4)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .buildSnippet();
    }

    @Override
    protected GlslFileEntry getMatricesShaderLib() {
        return new GlShaderLibraryBuilder<>(CometLoaders.STRING, getMatrixSnippet())
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
    protected void setupUIProjection(int scale) {
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
    protected void restoreUIProjection() {
        RenderSystem.setProjectionMatrix(
                this.matrix4fStack.peek(),
                VertexSorting.ORTHOGRAPHIC_Z
        );
        this.matrix4fStack.pop();
    }

    @Override
    protected void initMatrix() {
        CometRenderer.getGlobalProgram().consumeIfUniformPresent(
                "projMat",
                UniformType.MATRIX4,
                projectionUniform ->
                        projectionUniform.set(RenderSystem.getProjectionMatrix())
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
        return new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), RenderColor.TRANSLUCENT, 0);
    }
}
