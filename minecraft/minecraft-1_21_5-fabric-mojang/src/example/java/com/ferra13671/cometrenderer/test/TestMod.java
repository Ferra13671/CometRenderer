package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.testing.BoxRenderTest;
import com.ferra13671.cometrenderer.test.mixins.IGlBuffer;
import com.ferra13671.cometrenderer.plugins.minecraft.testing.UIRenderTest;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class TestMod implements Mc {

    public static void initRender() {
        CometRenderer.init();
        MinecraftPlugin.init(glGpuBuffer -> ((IGlBuffer) glGpuBuffer)._getHandle(), () -> 1);

        UIRenderTest.init();
    }

    public static void render() {
        if (mc.player == null)
            return;

        MinecraftPlugin.getInstance().bindMainFramebuffer(true);

        UIRenderTest.draw();
    }

    public static void renderBoxWithDepth() {
        if (mc.player == null)
            return;

        MinecraftPlugin.getInstance().bindMainFramebuffer(true);

        Vec3 vec1 = mc.player.calculateViewVector(mc.player.getXRot(), mc.player.getYRot() + 30).multiply(3, 3, 3);
        Vec3 vec2 = mc.player.calculateViewVector(mc.player.getXRot(), mc.player.getYRot() - 30).multiply(3, 3, 3);

        //Box with depth (White)
        GlStateManager._enableDepthTest();
        GlStateManager._depthMask(false);
        BoxRenderTest.draw(vec1.toVector3f(), com.ferra13671.cometrenderer.plugins.minecraft.RenderColor.WHITE);

        //Color without depth (Cyan)
        GlStateManager._disableDepthTest();
        BoxRenderTest.draw(vec2.toVector3f(), com.ferra13671.cometrenderer.plugins.minecraft.RenderColor.of(Color.CYAN));
    }
}
