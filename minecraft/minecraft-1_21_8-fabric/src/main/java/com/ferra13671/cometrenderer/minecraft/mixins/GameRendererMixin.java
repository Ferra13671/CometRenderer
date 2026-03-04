package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderWorldCallback;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
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
        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(true);

        RenderHudCallback.EVENT.invoker().onRenderHud();
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V", shift = At.Shift.AFTER))
    public void modifyRenderLevelAfterRenderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local(name = "matrix4f3") Matrix4f rotationMatrix) {
        if (minecraft.player == null)
            return;

        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(true);
        RenderSystem.getModelViewStack().pushMatrix().mul(rotationMatrix);

        RenderWorldCallback.EVENT.invoker().onRenderWorld();

        RenderSystem.getModelViewStack().popMatrix();
    }
}
