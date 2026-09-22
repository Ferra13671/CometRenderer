package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindable;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindableBase;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable.UBOBindableRange;
import com.ferra13671.cometrenderer.sampler.unit.SamplerUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.TextureUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.UnitBindable;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.MeshData;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

@API(status = API.Status.MAINTAINED, since = "3.0")
@UtilityClass
public class MinecraftEXT {
    private final BufferRenderer<MeshData> renderer = (builtBuffer, close) -> {
        MeshData.DrawState drawState = builtBuffer.drawState();

        if (drawState.indexCount() > 0) {
            drawState.format().getImmediateDrawVertexBuffer().upload(builtBuffer);
            drawState.format().getImmediateDrawVertexBuffer().draw();
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
        return new UnitBindable[]{
                abstractTexture == null ? TextureUnitBindable.EMPTY : new TextureUnitBindable(abstractTexture.getId()),
                SamplerUnitBindable.EMPTY
        };
    }

    public @Nullable UBOBindable getUBOBindable(@Nullable GpuBuffer buffer) {
        return buffer == null ? null : new UBOBindableBase(buffer.handle);
    }

    public @Nullable UBOBindable getUBOBindable(@Nullable GpuBuffer buffer, long offset, long size) {
        return buffer == null ? null : new UBOBindableRange(buffer.handle, offset, size);
    }
}
