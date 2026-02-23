package com.ferra13671.cometrenderer.testmod;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.gltextureutils.*;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import com.ferra13671.gltextureutils.loader.FileEntry;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import com.ferra13671.cometrenderer.minecraft.batch.IPrimitiveBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.BasicTextureBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.BasicRectBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.ColoredRectBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.RoundedTextureBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.RoundedRectBatch;

import java.awt.*;

public final class UIRenderTest {
    private static GLTexture texture;
    public static IPrimitiveBatch standaloneDrawer;

    public static void init() {
        texture =
                TextureLoaders.FILE_ENTRY.createTextureBuilder()
                        .name("Test-texture")
                        .info(new FileEntry("texture.jpg", PathMode.INSIDE_JAR))
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();

        standaloneDrawer = new BasicTextureBatch()
                .setTexture(texture)
                .rectSized(600, 100, 100, 100, new TextureBorder(0, 0, 1, 1))
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
        new BasicRectBatch(() -> CometRenderer.getShaderColor().setColor(RenderColor.of(Color.RED).toVector4f()))
                .rectSized(200, 100, 100, 100)
                .build()
                .tryDraw()
                .close();
    }

    private static void drawMultiColorRect() {
        new ColoredRectBatch()
                .rectSized(200, 210, 100, 100, new RectColors(
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

        new RoundedTextureBatch()
                .setTexture(texture)
                .rectSized(600, 210, 100, 100, 20, RectColors.oneColor(RenderColor.WHITE), new TextureBorder(0, 0, 1, 1))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawRoundedRects() {
        new RoundedRectBatch()
                .rectSized(400, 100, 100, 100, 20, RectColors.oneColor(RenderColor.of(Color.MAGENTA)))
                .rectSized(400, 210, 100, 100, 20, RectColors.horizontalGradient(RenderColor.of(Color.BLUE), RenderColor.of(Color.CYAN)))
                .build()
                .tryDraw()
                .close();
    }
}
