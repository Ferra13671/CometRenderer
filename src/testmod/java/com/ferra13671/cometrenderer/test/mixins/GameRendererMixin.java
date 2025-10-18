package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.test.RenderUtils;
import com.ferra13671.cometrenderer.test.TestMod;
import com.ferra13671.cometrenderer.test.TestPostEffect;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyArg(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"), index = 2)
    public VertexConsumerProvider.Immediate modifyArgInRenderItemInRenderHand(VertexConsumerProvider.Immediate vertexConsumers) {
        return TestPostEffect.handsVertexConsumer;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogRenderer;rotate()V", shift = At.Shift.BEFORE))
    public void modifyRenderBeforeGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        RenderUtils.unscaledProjection();

        GlStateManager._disableDepthTest();
        RenderSystem.resetTextureMatrix();

        TestMod.render();

        RenderUtils.scaledProjection();
    }
}
