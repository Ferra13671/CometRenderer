package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.test.TestMod;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "initRenderer", at = @At("TAIL"))
    private static void modifyInitRender(CallbackInfo ci) {
        TestMod.initRender();
    }
}
