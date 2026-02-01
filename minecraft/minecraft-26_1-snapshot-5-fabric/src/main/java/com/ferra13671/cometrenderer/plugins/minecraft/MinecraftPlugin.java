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
import com.mojang.blaze3d.GraphicsWorkarounds;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class MinecraftPlugin extends AbstractMinecraftPlugin {
    @Getter
    private static MinecraftPlugin instance;
    @Getter
    private final Function<GlBuffer, Integer> bufferIdGetter;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger("CometRenderer");
    private final VertexArrayCache vertexArrayCache;
    @Getter
    private final DirectStateAccess directStateAccess;
    public final BufferRenderer<MeshData> MINECRAFT_BUFFER;
    private final CachedOrthoProjectionMatrixBuffer uiMatrix;

    private MinecraftPlugin(Function<GlBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        super(scaleGetter);

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
        this.MINECRAFT_BUFFER = (builtBuffer, close) -> {
            MeshData.DrawState drawState = builtBuffer.drawState();

            if (drawState.indexCount() > 0) {
                RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawState.mode());

                GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex mesh", 40, builtBuffer.vertexBuffer());
                GpuBuffer indexBuffer = shapeIndexBuffer.getBuffer(drawState.indexCount());
                VertexFormat.IndexType indexType = shapeIndexBuffer.type();

                this.vertexArrayCache.bindVertexArray(
                        drawState.format(),
                        (GlBuffer) vertexBuffer
                );
                GL15.glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, getBufferIdGetter().apply((GlBuffer) indexBuffer));
                GL11.glDrawElements(
                        GlConst.toGl(drawState.mode()),
                        drawState.indexCount(),
                        GlConst.toGl(indexType),
                        0
                );

                vertexBuffer.close();
            }
            if (close)
                builtBuffer.close();
        };

        this.bufferIdGetter = bufferIdGetter;
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

        this.uiMatrix = new CachedOrthoProjectionMatrixBuffer("ui-matrix", -1000, 1000, true);
    }

    public static void init(Function<GlBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        if (instance != null)
            throw new IllegalStateException("Minecraft plugin already initialized");

        instance = new MinecraftPlugin(bufferIdGetter, scaleGetter);
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
    public void setupUIProjection() {
        float scale = scaleGetter.get();

        RenderSystem.setProjectionMatrix(
                uiMatrix.getBuffer(
                        Minecraft.getInstance().getWindow().getWidth() / scale,
                        Minecraft.getInstance().getWindow().getHeight() / scale
                ),
                ProjectionType.ORTHOGRAPHIC
        );
    }

    @Override
    public void initMatrix() {
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
    protected void createMainFramebuffer() {
        this.mainFrameBuffer = new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), new Color(0, 0, 0, 0), 0);
    }

    public void draw(MeshData builtBuffer) {
        draw(builtBuffer, true);
    }

    public void draw(MeshData builtBuffer, boolean close) {
        CometRenderer.draw(MINECRAFT_BUFFER, builtBuffer, close);
    }
}
