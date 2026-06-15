package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.mojang.blaze3d.opengl.GlProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlCommandEncoder.class)
public interface IGlCommandEncoder {

    @Accessor("lastProgram")
    void crm$$$setLastProgram(GlProgram id);
}
