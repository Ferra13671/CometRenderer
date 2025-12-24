package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.test.RenderUtils;
import com.ferra13671.cometrenderer.test.TestMod;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;clear()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        RenderUtils.unscaledProjection();

        GlStateManager._disableDepthTest();

        TestMod.render();

        RenderUtils.scaledProjection();
    }
}
