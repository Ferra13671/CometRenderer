package com.ferra13671.cometrenderer.test.mixins;

import net.minecraft.client.gl.GlGpuBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlGpuBuffer.class)
public interface IGlGpuBuffer {

    @Accessor("id")
    int _getId();
}
