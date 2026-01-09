package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.ferra13671.cometrenderer.utils.Logger;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.ProjectionMatrix2;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class MinecraftPlugin extends AbstractMinecraftPlugin {
    @Getter
    private static MinecraftPlugin instance;
    @Getter
    private final Function<GlGpuBuffer, Integer> bufferIdGetter;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger("CometRenderer");
    public final BufferRenderer<BuiltBuffer> MINECRAFT_BUFFER = (builtBuffer, close) -> {
        BuiltBuffer.DrawParameters drawState = builtBuffer.getDrawParameters();

        if (drawState.indexCount() > 0) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawState.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex mesh", 40, builtBuffer.getBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(drawState.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

            ((GlBackend) RenderSystem.getDevice()).getVertexBufferManager().setupBuffer(
                    drawState.format(),
                    (GlGpuBuffer) vertexBuffer
            );
            GL15.glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, getBufferIdGetter().apply((GlGpuBuffer) indexBuffer));
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
    private final ProjectionMatrix2 uiMatrix;

    private MinecraftPlugin(Function<GlGpuBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        super(scaleGetter);

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

        this.uiMatrix = new ProjectionMatrix2("ui-matrix", -1000, 1000, true);
    }

    public static void init(Function<GlGpuBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        if (instance != null)
            throw new IllegalStateException("Minecraft plugin already initialized");

        instance = new MinecraftPlugin(bufferIdGetter, scaleGetter);
    }

    @Override
    public void setupUIProjection() {
        float scale = scaleGetter.get();

        RenderSystem.setProjectionMatrix(
                uiMatrix.set(
                        MinecraftClient.getInstance().getWindow().getWidth() / scale,
                        MinecraftClient.getInstance().getWindow().getHeight() / scale
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
    public void bindMainFramebuffer(boolean setViewport) {
        if (mainFrameBuffer == null) {
            if (MinecraftClient.getInstance().getFramebuffer() != null)
                mainFrameBuffer = new MinecraftFramebuffer(MinecraftClient.getInstance().getFramebuffer(), new Color(0, 0, 0, 0), 0);
        } else
            mainFrameBuffer.bind(setViewport);
    }

    public void draw(BuiltBuffer builtBuffer) {
        draw(builtBuffer, true);
    }

    public void draw(BuiltBuffer builtBuffer, boolean close) {
        CometRenderer.draw(MINECRAFT_BUFFER, builtBuffer, close);
    }
}
