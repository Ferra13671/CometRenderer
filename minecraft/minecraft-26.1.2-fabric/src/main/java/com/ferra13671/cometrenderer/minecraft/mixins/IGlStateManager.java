package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlStateManager.class)
public interface IGlStateManager {

    @Accessor("activeTexture")
    static int crm$$$getActiveTexture() {
        throw new IllegalStateException();
    }
}
