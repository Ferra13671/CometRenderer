package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.RectColors;
import com.ferra13671.cometrenderer.plugins.minecraft.RenderColor;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.IDrawer;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl.BasicRectDrawer;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl.BasicTextureDrawer;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl.ColoredRectDrawer;
import com.ferra13671.cometrenderer.test.mixins.IGlBuffer;
import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import com.ferra13671.gltextureutils.builder.GLTextureInfo;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import org.joml.Vector4f;

import java.util.Random;

public class TestMod implements Mc {

    private static final TextureLoader<String> textureLoader = new TextureLoader<>() {
        @Override
        public GLTextureInfo load(String s, ColorMode colorMode) throws Exception {
            return TextureLoaders.INPUT_STREAM.load(TestMod.class.getClassLoader().getResourceAsStream(s), colorMode);
        }
    };
    private static GLTexture texture;

    public static IDrawer standaloneDrawer;

    public static void initRender() {
        CometRenderer.init();
        MinecraftPlugin.init(glGpuBuffer -> ((IGlBuffer) glGpuBuffer)._getHandle(), () -> mc.getWindow().getGuiScale());

        texture =
                textureLoader.createTextureBuilder()
                        .name("Test-texture")
                        .info("texture.jpg")
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();

        standaloneDrawer = new BasicTextureDrawer()
                .setTexture(texture)
                .rectSized(400, 250, 50, 50, new TextureBorder(0, 0, 1, 1))
                .build()
                .makeStandalone();
    }

    public static void render() {
        if (mc.player == null)
            return;

        MinecraftPlugin.getInstance().bindMainFramebuffer(true);

        drawRandomColorRect();

        drawMultiColorRect();

        drawTextureRect();
    }

    private static void drawRandomColorRect() {
        Random random = new Random();
        new BasicRectDrawer(() -> CometRenderer.setShaderColor(new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f)))
                .rectSized(200, 200, 200, 200)
                .build()
                .tryDraw()
                .close();
    }

    private static void drawMultiColorRect() {
        new ColoredRectDrawer()
                .rectSized(400, 200, 50, 50, new RectColors(
                        RenderColor.of(1f, 1f, 1f, 1f),
                        RenderColor.of(1f, 0f, 0f, 1f),
                        RenderColor.of(0f, 1f, 0f, 1f),
                        RenderColor.of(0f, 0f, 1f, 1f)
                ))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawTextureRect() {
        standaloneDrawer.tryDraw();
    }
}
