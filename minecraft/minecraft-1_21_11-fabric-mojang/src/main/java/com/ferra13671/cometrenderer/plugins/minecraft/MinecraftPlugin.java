package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.BufferRenderer;
import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.Logger;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.plugins.minecraft.program.DefaultPrograms;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.utils.scissor.ScissorRect;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class MinecraftPlugin {
    @Getter
    private static Function<GlBuffer, Integer> bufferIdGetter;
    private static Supplier<Integer> scaleGetter;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("CometRenderer");
    @Getter
    private static final GlProgramSnippet matrixSnippet = CometLoaders.IN_JAR.createProgramBuilder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX4)
            .buildSnippet();
    public static final BufferRenderer<MeshData> MINECRAFT_BUFFER = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawState.mode());

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "CometRenderer vertex mesh", 40, builtBuffer.vertexBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getBuffer(drawState.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.type();

            ((GlDevice) RenderSystem.getDevice()).vertexArrayCache().bindVertexArray(
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
    private static Framebuffer mainFrameBuffer;
    private static CachedOrthoProjectionMatrixBuffer uiMatrix;
    @Getter
    private static DefaultPrograms programs;

    public static void init(Function<GlBuffer, Integer> bufferIdGetter, Supplier<Integer> scaleGetter) {
        MinecraftPlugin.bufferIdGetter = bufferIdGetter;
        MinecraftPlugin.scaleGetter = scaleGetter;
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

        uiMatrix = new CachedOrthoProjectionMatrixBuffer("ui-matrix", -1000, 1000, true);
        programs = new DefaultPrograms();
    }

    public static void setupUIProjection() {
        float scale = scaleGetter.get();

        RenderSystem.setProjectionMatrix(
                uiMatrix.getBuffer(
                        Minecraft.getInstance().getWindow().getWidth() / scale,
                        Minecraft.getInstance().getWindow().getHeight() / scale
                ),
                ProjectionType.ORTHOGRAPHIC
        );
    }

    public static void initMatrix() {
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

    public static void bindMainFramebuffer(boolean setViewport) {
        if (mainFrameBuffer == null) {
            if (Minecraft.getInstance().getMainRenderTarget() != null)
                mainFrameBuffer = new MinecraftFramebuffer(Minecraft.getInstance().getMainRenderTarget(), new Color(0, 0, 0, 0), 0);
        } else
            mainFrameBuffer.bind(setViewport);
    }

    public static ScissorRect fixScissorRect(ScissorRect scissorRect) {
        int scale = scaleGetter.get();
        int frameBufferHeight = Minecraft.getInstance().getWindow().getHeight();
        return new ScissorRect(
                scissorRect.x() * scale,
                frameBufferHeight - ((scissorRect.y() + scissorRect.height()) * scale),
                scissorRect.width() * scale,
                scissorRect.height() * scale
        );
    }

    public static void draw(MeshData builtBuffer) {
        draw(builtBuffer, true);
    }

    public static void draw(MeshData builtBuffer, boolean close) {
        CometRenderer.draw(MINECRAFT_BUFFER, builtBuffer, close);
    }
}
