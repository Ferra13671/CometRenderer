package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.test.TestMod;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;drawCurrentLayer()V", ordinal = 2, shift = At.Shift.AFTER))
    public void modifyRenderWorldBeforePopView(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        CometRenderer.applyDefaultBlend();

        TestMod.renderBoxWithDepth();
    }
}
