package com.ferra13671.cometrenderer.testmod;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import com.ferra13671.cometrenderer.minecraft.CRMInstance;
import com.ferra13671.cometrenderer.minecraft.event.AfterInitializeCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderHudCallback;
import com.ferra13671.cometrenderer.minecraft.event.RenderWorldCallback;
import com.ferra13671.cometrenderer.minecraft.RenderColor;

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
            instance.setupUIMatrix();
            UIRenderTest.draw();
            instance.restoreUIMatrix();
        });
        RenderWorldCallback.EVENT.register(this::renderBoxes);
    }

    public void renderBoxes() {

        Vec3 vec1 = calculateViewVector(mc.player.getXRot(), mc.player.getYRot() + 30).multiply(3, 3, 3);
        Vec3 vec2 = calculateViewVector(mc.player.getXRot(), mc.player.getYRot() - 30).multiply(3, 3, 3);

        //Box with depth (White)
        GlStateManager._enableDepthTest();
        GlStateManager._depthMask(false);
        BoxRenderTest.draw(vec1.toVector3f(), RenderColor.WHITE);

        //Color without depth (Cyan)
        GlStateManager._disableDepthTest();
        BoxRenderTest.draw(vec2.toVector3f(), RenderColor.of(Color.CYAN));
    }

    private Vec3 calculateViewVector(float f, float g) {
        float h = f * (float) (Math.PI / 180.0);
        float i = -g * (float) (Math.PI / 180.0);
        float j = Mth.cos(i);
        float k = Mth.sin(i);
        float l = Mth.cos(h);
        float m = Mth.sin(h);
        return new Vec3(k * l, -m, j * l);
    }
}
