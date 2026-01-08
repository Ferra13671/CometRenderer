package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.test.TestMod;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;reset()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean tick, CallbackInfo ci) {
        MinecraftPlugin.setupUIProjection();
        GlStateManager._disableDepthTest();

        TestMod.render();
    }
}
