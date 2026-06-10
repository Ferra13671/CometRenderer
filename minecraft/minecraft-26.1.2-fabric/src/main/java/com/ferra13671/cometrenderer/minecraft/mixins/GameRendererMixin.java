package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderWorldCallback;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", shift = At.Shift.BEFORE))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (minecraft.player == null)
            return;

        GlStateManager._disableDepthTest();
        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(false);

        RenderHudCallback.EVENT.invoker().onRenderHud();
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;ZLnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)V", shift = At.Shift.AFTER))
    public void modifyRenderLevelAfterRenderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local(name = "modelViewMatrix") Matrix4fc rotationMatrix) {
        if (minecraft.player == null)
            return;

        CometRenderer.setDefaultBlend();
        CRM.getMainFramebuffer().bind(false);
        RenderSystem.getModelViewStack().pushMatrix().mul(rotationMatrix);

        RenderWorldCallback.EVENT.invoker().onRenderWorld();

        RenderSystem.getModelViewStack().popMatrix();
    }
}
