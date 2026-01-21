package com.ferra13671.cometrenderer.plugins.minecraft.testing;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.RectColors;
import com.ferra13671.cometrenderer.plugins.minecraft.RenderColor;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.IDrawer;
import com.ferra13671.cometrenderer.plugins.minecraft.drawer.impl.*;
import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import com.ferra13671.gltextureutils.builder.GLTextureInfo;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import com.ferra13671.gltextureutils.loader.TextureLoaders;

import java.awt.*;

public final class UIRenderTest {
    private static final TextureLoader<String> textureLoader = new TextureLoader<>() {
        @Override
        public GLTextureInfo load(String s, ColorMode colorMode) throws Exception {
            return TextureLoaders.INPUT_STREAM.load(UIRenderTest.class.getClassLoader().getResourceAsStream(s), colorMode);
        }
    };

    private static GLTexture texture;
    public static IDrawer standaloneDrawer;

    public static void init() {
        texture =
                textureLoader.createTextureBuilder()
                        .name("Test-texture")
                        .info("texture.jpg")
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();

        standaloneDrawer = new BasicTextureDrawer()
                .setTexture(texture)
                .rectSized(1200, 400, 100, 100, new TextureBorder(0, 0, 1, 1))
                .build()
                .makeStandalone();
    }

    public static void draw() {
        drawOneColorRect();
        drawMultiColorRect();
        drawTextures();
        drawRoundedRects();
    }

    private static void drawOneColorRect() {
        new BasicRectDrawer(() -> CometRenderer.getShaderColor().setColor(RenderColor.of(Color.RED).toVector4f()))
                .rectSized(800, 400, 100, 100)
                .build()
                .tryDraw()
                .close();
    }

    private static void drawMultiColorRect() {
        new ColoredRectDrawer()
                .rectSized(800, 510, 100, 100, new RectColors(
                        RenderColor.of(1f, 1f, 1f, 1f),
                        RenderColor.of(1f, 0f, 0f, 1f),
                        RenderColor.of(0f, 1f, 0f, 1f),
                        RenderColor.of(0f, 0f, 1f, 1f)
                ))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawTextures() {
        standaloneDrawer.tryDraw();

        new RoundedTextureDrawer()
                .setTexture(texture)
                .rectSized(1200, 510, 100, 100, 20, RectColors.oneColor(RenderColor.WHITE), new TextureBorder(0, 0, 1, 1))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawRoundedRects() {
        new RoundedRectDrawer()
                .rectSized(1000, 400, 100, 100, 20, RectColors.oneColor(RenderColor.of(Color.MAGENTA)))
                .rectSized(1000, 510, 100, 100, 20, RectColors.horizontalGradient(RenderColor.of(Color.BLUE), RenderColor.of(Color.CYAN)))
                .build()
                .tryDraw()
                .close();
    }
}
