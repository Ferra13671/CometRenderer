package com.ferra13671.cometrenderer.test.mixins;

import net.minecraft.client.render.ProjectionMatrix2;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ProjectionMatrix2.class)
public interface ProjectionMatrix2Accessor {

    @Invoker("getMatrix")
    Matrix4f _getMatrix(float width, float height);
}
