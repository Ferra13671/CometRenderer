package com.ferra13671.cometrenderer.testmod;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.batch.impl.text.RenderText;
import com.ferra13671.cometrenderer.minecraft.blur.BlurProvider;
import com.ferra13671.gltextureutils.*;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import com.ferra13671.gltextureutils.loader.FileEntry;
import com.ferra13671.cometrenderer.minecraft.batch.IPrimitiveBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.*;
import com.ferra13671.cometrenderer.minecraft.font.TTFFont;
import com.ferra13671.cometrenderer.minecraft.batch.impl.text.TextBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.RoundedBlurBatch;
import com.ferra13671.cometrenderer.minecraft.blur.BlurConfig;
import com.ferra13671.gltextureutils.loader.TextureLoader;

import java.awt.*;

public final class UIRenderTest {
    private static GLTexture texture;
    private static TTFFont font;
    private static final BlurProvider blurProvider = new BlurProvider(BlurConfig.DEFAULT);
    public static IPrimitiveBatch standaloneDrawer;

    public static void init() {
        texture =
                TextureLoader.FILE_ENTRY.createTextureBuilder()
                        .name("Test-texture")
                        .info(new FileEntry("texture.jpg", PathMode.INSIDE_JAR))
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();

        try {
            font = new TTFFont(Font.createFont(Font.TRUETYPE_FONT, UIRenderTest.class.getClassLoader().getResourceAsStream("test-font.otf")).deriveFont(26f));
        } catch (Exception e) {
            e.printStackTrace();
        }

        standaloneDrawer = new BasicTextureBatch()
                .setTexture(texture)
                .rectSized(460, 100, 100, 100, new TextureBorder(0, 0, 1, 1))
                .build()
                .makeStandalone();
    }

    public static void draw() {
        blurProvider.blurFrame();

        drawOneColorRect();
        drawMultiColorRect();
        drawTextures();
        drawRoundedRects();
        drawText();
        drawBlur();
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
                .rectSized(460, 210, 100, 100, 20, RectColors.oneColor(RenderColor.WHITE), new TextureBorder(0, 0, 1, 1))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawRoundedRects() {
        new RoundedRectBatch()
                .rectSized(330, 100, 100, 100, 20, RectColors.oneColor(RenderColor.of(Color.MAGENTA)))
                .rectSized(330, 210, 100, 100, 20, RectColors.horizontalGradient(RenderColor.of(Color.BLUE), RenderColor.of(Color.CYAN)))
                .build()
                .tryDraw()
                .close();
    }

    private static void drawBlur() {
        new BasicBlurBatch(blurProvider)
                .rectSized(590, 100, 100, 100)
                .build()
                .tryDraw()
                .close();

        new RoundedBlurBatch(blurProvider)
                .rectSized(590, 210, 100, 100, 20)
                .build()
                .tryDraw()
                .close();
    }

    private static void drawText() {
        float x = 200;
        float y = 350;

        new TextBatch(font)
                .text(
                        new RenderText("Example text", x, y)
                )
                .text(
                        new RenderText("text with shadow", x, y + font.getTextHeight())
                                .withColor(RenderColor.of(Color.YELLOW))
                                .withShadow(true)
                )
                .text(
                        new RenderText("абвгде§cёжзий", x, y + (font.getTextHeight() * 2))
                )
                .build()
                .tryDraw()
                .close();
    }
}
