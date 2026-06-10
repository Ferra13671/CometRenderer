package com.ferra13671.cometrenderer.minecraft.mixins;

import com.mojang.blaze3d.opengl.GlConst;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GlConst.class)
public class GlConstMixin {

    @ModifyConstant(method = "toGlInternalId", constant = @Constant(intValue = GL30.GL_DEPTH_COMPONENT32))
    private static int modifyInternalIdInGlConst(int constant) {
        return GL30.GL_DEPTH24_STENCIL8;
    }

    @ModifyConstant(method = "toGlExternalId", constant = @Constant(intValue = GL30.GL_DEPTH_COMPONENT))
    private static int modifyExternalIdInGlConst(int constant) {
        return GL30.GL_DEPTH_STENCIL;
    }

    @ModifyConstant(method = "toGlType", constant = @Constant(intValue = 5126))
    private static int modifyTypeInGlConst(int constant) {
        return GL30.GL_UNSIGNED_INT_24_8;
    }
}
