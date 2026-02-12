package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.test.TestMod;
import com.ferra13671.cometrenderer.plugins.minecraft.VAOState;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", ordinal = 0, shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        VAOState.push();

        MinecraftPlugin.getInstance().setupUIProjection();
        CometRenderer.applyDefaultBlend();

        GlStateManager._disableDepthTest();

        TestMod.render();

        MinecraftPlugin.getInstance().restoreProjection();
        CometRenderer.disableBlend();
        VAOState.pop();
    }
}
