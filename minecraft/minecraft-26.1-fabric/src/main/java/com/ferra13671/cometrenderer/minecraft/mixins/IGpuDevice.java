package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.GpuDeviceBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GpuDevice.class)
public interface IGpuDevice {

    @Accessor("backend")
    GpuDeviceBackend crm$$$getBackend();
}
