package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.ferra13671.cometrenderer.utils.Logger;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.slf4j.LoggerFactory;

import java.util.Stack;
import java.util.function.Supplier;

public class MinecraftPlugin extends AbstractMinecraftPlugin {
    @Getter
    private static MinecraftPlugin instance;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger("CometRenderer");
    public final BufferRenderer<MeshData> MINECRAFT_BUFFER = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            drawState.format().getImmediateDrawVertexBuffer().upload(builtBuffer);
            drawState.format().getImmediateDrawVertexBuffer().draw();
        }
        if (close)
            builtBuffer.close();
    };
    private final Stack<Matrix4f> matrix4fStack = new Stack<>();

    private MinecraftPlugin(Supplier<Integer> scaleGetter) {
        super(scaleGetter);

        CometRenderer.setLogger(new Logger() {
            @Override
            public void log(String message) {
                logger.info(message);
            }

            @Override
            public void warn(String message) {
                logger.warn(message);
            }

            @Override
            public void error(String message) {
                logger.error(message);
            }
        });
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

    public static void init(Supplier<Integer> scaleGetter) {
        if (instance != null)
            throw new IllegalStateException("Minecraft plugin already initialized");

        instance = new MinecraftPlugin(scaleGetter);
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
    public void setupUIProjection() {
        float scale = scaleGetter.get();

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
                ProjectionType.ORTHOGRAPHIC
        );
    }

    public void restoreProjection() {
        RenderSystem.setProjectionMatrix(
                this.matrix4fStack.peek(),
                ProjectionType.ORTHOGRAPHIC
        );
        this.matrix4fStack.pop();
    }

    @Override
    public void initMatrix() {
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
    protected void createMainFramebuffer() {
        this.mainFrameBuffer = new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), RenderColor.TRANSLUCENT, 0);
    }

    public void draw(MeshData builtBuffer) {
        draw(builtBuffer, true);
    }

    public void draw(MeshData builtBuffer, boolean close) {
        CometRenderer.draw(MINECRAFT_BUFFER, builtBuffer, close);
    }
}
