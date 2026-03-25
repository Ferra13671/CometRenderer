package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.minecraft.CRMController;
import com.ferra13671.cometrenderer.minecraft.event.AfterInitializeCallback;
import com.ferra13671.cometrenderer.minecraft.event.BeforeInitializeCallback;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "initRenderer", at = @At("TAIL"))
    private static void modifyInitRender(CallbackInfo ci) {
        BeforeInitializeCallback.EVENT.invoker().onBeforeInitialize();
        CRMController.initMod();
        AfterInitializeCallback.EVENT.invoker().onAfterInitialize();
    }
}
