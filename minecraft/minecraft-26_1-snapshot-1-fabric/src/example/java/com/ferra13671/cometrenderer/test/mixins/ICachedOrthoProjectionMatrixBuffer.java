package com.ferra13671.cometrenderer.test.mixins;

import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CachedOrthoProjectionMatrixBuffer.class)
public interface ICachedOrthoProjectionMatrixBuffer {

    @Invoker("createProjectionMatrix")
    Matrix4f _createProjectionMatrix(float width, float height);
}
