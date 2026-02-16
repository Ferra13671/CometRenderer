package com.ferra13671.cometrenderer.testmod;

import com.ferra13671.cometrenderer.minecraft.event.AfterInitializeCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.world.phys.Vec3;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.CRMInstance;
import com.ferra13671.cometrenderer.minecraft.event.RenderWorldCallback;

import java.awt.*;

public class TestMod implements PreLaunchEntrypoint, Mc {
    private CRMInstance instance;

    @Override
    public void onPreLaunch() {
        AfterInitializeCallback.EVENT.register(() -> {
            instance = new CRMInstance(() -> 1);
            UIRenderTest.init();
        });

        RenderHudCallback.EVENT.register(() -> {
            instance.setupUIProjection();
            UIRenderTest.draw();
        });
        RenderWorldCallback.EVENT.register(this::renderBoxes);
    }

    public void renderBoxes() {

        Vec3 vec1 = mc.player.calculateViewVector(mc.player.getXRot(), mc.player.getYRot() + 30).multiply(3, 3, 3);
        Vec3 vec2 = mc.player.calculateViewVector(mc.player.getXRot(), mc.player.getYRot() - 30).multiply(3, 3, 3);

        //Box with depth (White)
        GlStateManager._enableDepthTest();
        GlStateManager._depthMask(false);
        BoxRenderTest.draw(vec1.toVector3f(), RenderColor.WHITE);

        //Color without depth (Cyan)
        GlStateManager._disableDepthTest();
        BoxRenderTest.draw(vec2.toVector3f(), RenderColor.of(Color.CYAN));
    }
}
