package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.textures.TextureFormat;
import net.irisshaders.iris.targets.RenderTargets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(RenderTargets.class)
public class IrisRenderTargetsMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;Lcom/mojang/blaze3d/textures/TextureFormat;III)Lcom/mojang/blaze3d/textures/GpuTexture;"), index = 1)
    public TextureFormat modifyArgInCreateTextureInInit(TextureFormat textureFormat) {
        return textureFormat == null ? TextureFormat.DEPTH32 : textureFormat;
    }
}
