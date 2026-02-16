package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;reset()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean tick, CallbackInfo ci) {
        if (minecraft.player == null)
            return;

        GlStateManager._disableDepthTest();
        CometRenderer.applyDefaultBlend();
        CRM.bindMainFramebuffer(true);

        RenderHudCallback.EVENT.invoker().onRenderHud();
    }
}
