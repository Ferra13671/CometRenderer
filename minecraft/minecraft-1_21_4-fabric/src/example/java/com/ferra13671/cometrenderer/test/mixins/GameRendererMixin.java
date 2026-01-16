package com.ferra13671.cometrenderer.test.mixins;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.test.TestMod;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", ordinal = 0, shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        //TODO maybe do something with this
        int currentVAO = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);

        MinecraftPlugin.getInstance().setupUIProjection();
        CometRenderer.applyDefaultBlend();

        GlStateManager._disableDepthTest();

        TestMod.render();

        MinecraftPlugin.getInstance().restoreProjection();
        CometRenderer.disableBlend();
        GL30.glBindVertexArray(currentVAO);
    }
}
