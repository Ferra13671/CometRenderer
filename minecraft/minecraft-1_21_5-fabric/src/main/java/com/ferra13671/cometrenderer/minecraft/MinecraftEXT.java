package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.mixins.IGlBuffer;
import com.ferra13671.cometrenderer.sampler.unit.SamplerUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.TextureUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.UnitBindable;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

@API(status = API.Status.MAINTAINED, since = "3.0")
@UtilityClass
public class MinecraftEXT {
    private final BufferRenderer<MeshData> renderer = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(drawState.mode());

            GpuBuffer vertexBuffer = drawState.format().uploadImmediateVertexBuffer(builtBuffer.vertexBuffer());
            GpuBuffer indexBuffer = shapeIndexBuffer.getBuffer(drawState.indexCount());
            VertexFormat.IndexType indexType = shapeIndexBuffer.type();

            GL15.glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, ((IGlBuffer) indexBuffer)._getHandle());
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

    public void draw(MeshData meshData) {
        draw(meshData, true);
    }

    public void draw(MeshData meshData, boolean close) {
        CometRenderer.draw(renderer, meshData, close);
    }

    public UnitBindable[] getTextureSampler(@Nullable AbstractTexture abstractTexture) {
        return abstractTexture == null ?
                new UnitBindable[]{
                        TextureUnitBindable.EMPTY,
                        SamplerUnitBindable.EMPTY
                }
                :
                getTextureSampler((GlTexture) abstractTexture.getTexture());
    }

    public UnitBindable[] getTextureSampler(@Nullable GlTexture texture) {
        return new UnitBindable[]{
                texture == null ?
                        TextureUnitBindable.EMPTY
                        :
                        new MinecraftTextureUnitBindable(texture)
                        ,
                SamplerUnitBindable.EMPTY
        };
    }

    private record MinecraftTextureUnitBindable(GlTexture texture) implements UnitBindable {

        @Override
        public void bind(int unit) {
            CometRenderer.getDevice().getPipelineStateManager().bindTexture(unit, texture().glId());
            texture().flushModeChanges();
        }
    }
}
