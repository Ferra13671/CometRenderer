package com.ferra13671.cometrenderer.minecraft.mixins;

import com.ferra13671.cometrenderer.State;
import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlTexture.class)
public class GlTextureMixin {

    @Inject(method = "method_68425", at = @At("RETURN"))
    public void modifyCreateFBO(DirectStateAccess directStateAccess, int i, int j, CallbackInfoReturnable<Integer> cir) {
        State.FRAMEBUFFER.bindFramebuffer(cir.getReturnValue(), false, 0, 0);
        GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_STENCIL_ATTACHMENT, GL11.GL_TEXTURE_2D, i, 0);
        State.FRAMEBUFFER.bindFramebuffer(0, false, 0, 0);
    }
}
