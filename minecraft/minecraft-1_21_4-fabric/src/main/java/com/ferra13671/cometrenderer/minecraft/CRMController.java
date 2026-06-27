package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GLProgramBuilder;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;

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
        State.FRAMEBUFFER = (id, viewport, width, height) -> {
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
            if (viewport)
                GlStateManager._viewport(0,0, width, height);
        };
    }

    @Override
    protected GLProgramSnippet loadMatrixSnippet() {
        return new GLProgramBuilder<>()
                .uniform("projMat", UniformType.MATRIX4)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .buildSnippet();
    }

    @Override
    protected GLSLFileEntry getMatricesShaderLib() {
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
    protected void setupUIMatrix(int scale) {
        this.matrix4fStack.push(RenderSystem.getProjectionMatrix());
        RenderSystem.setProjectionMatrix(
                (new Matrix4f()).setOrtho(
                        0.0F,
                        (float)((double)Minecraft.getInstance().getWindow().getWidth() / scale),
                        (float)((double)Minecraft.getInstance().getWindow().getHeight() / scale),
                        0.0F,
                        1000.0F,
                        21000.0F
                ),
                ProjectionType.ORTHOGRAPHIC
        );
    }

    @Override
    protected void restoreUIMatrix() {
        RenderSystem.setProjectionMatrix(
                this.matrix4fStack.peek(),
                ProjectionType.ORTHOGRAPHIC
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
