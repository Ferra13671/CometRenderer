package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.VAOState;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderWorldCallback;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Final
    private Camera mainCamera;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V", ordinal = 0, shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(float f, long l, boolean bl, CallbackInfo ci) {
        VAOState.save();

        if (minecraft.player == null)
            return;

        GlStateManager._disableDepthTest();
        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(true);

        RenderHudCallback.EVENT.invoker().onRenderHud();

        CometRenderer.disableBlend();
        VAOState.load();
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    public void modifyRenderLevelAfterRenderLevel(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.player == null)
            return;

        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(true);
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().mulPose(Axis.XP.rotationDegrees(this.mainCamera.getXRot()));
        RenderSystem.getModelViewStack().mulPose(Axis.YP.rotationDegrees(this.mainCamera.getYRot() + 180.0F));
        RenderSystem.applyModelViewMatrix();

        RenderWorldCallback.EVENT.invoker().onRenderWorld();

        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(true);
        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
