package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.CommandEncoderBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandEncoder.class)
public interface ICommandEncoder {

    @Accessor("backend")
    CommandEncoderBackend crm$$$getBackend();
}
