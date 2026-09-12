package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.sampler.unit.SamplerUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.TextureUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.UnitBindable;
import com.ferra13671.cometrenderer.utils.BufferRenderer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

@API(status = API.Status.MAINTAINED, since = "3.0")
@UtilityClass
public class MinecraftEXT {
    private final BufferRenderer<BufferBuilder.RenderedBuffer> renderer = (renderedBuffer, close) -> {
        BufferBuilder.DrawState drawState = renderedBuffer.drawState();

        if (drawState.indexCount() > 0) {
            drawState.format().getImmediateDrawVertexBuffer().upload(renderedBuffer);
            drawState.format().getImmediateDrawVertexBuffer().draw();
        }
    };

    public void draw(BufferBuilder.RenderedBuffer renderedBuffer) {
        CometRenderer.draw(renderer, renderedBuffer, false);
    }

    public UnitBindable[] getTextureSampler(@Nullable AbstractTexture abstractTexture) {
        return new UnitBindable[]{
                abstractTexture == null ? TextureUnitBindable.EMPTY : new TextureUnitBindable(abstractTexture.getId()),
                SamplerUnitBindable.EMPTY
        };
    }
}
