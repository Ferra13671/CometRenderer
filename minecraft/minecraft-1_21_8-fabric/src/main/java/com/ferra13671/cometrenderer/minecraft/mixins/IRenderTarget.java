package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.pipeline.RenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTarget.class)
public interface IRenderTarget {

    @Accessor("label")
    String crm$$$getLabel();
}
