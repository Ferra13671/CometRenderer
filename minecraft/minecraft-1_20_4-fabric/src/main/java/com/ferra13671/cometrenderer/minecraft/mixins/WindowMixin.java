package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Window.class)
public class WindowMixin {
    @Unique
    private boolean shouldChangeMinorVersion = false;

    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V"))
    public void modifyArgsInWindowHintInInit(Args args) {
        if (args.get(0).equals(GLFW.GLFW_CONTEXT_VERSION_MAJOR))
            if ((Integer) args.get(1) == 3)
                this.shouldChangeMinorVersion = true;

        if (this.shouldChangeMinorVersion && args.get(0).equals(GLFW.GLFW_CONTEXT_VERSION_MINOR))
            args.set(1, 3);
    }
}
