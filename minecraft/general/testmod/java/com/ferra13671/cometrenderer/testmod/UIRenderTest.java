package com.ferra13671.cometrenderer.testmod;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.StencilInfo;
import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.batch.impl.text.RenderText;
import com.ferra13671.cometrenderer.minecraft.blur.BlurPass;
import com.ferra13671.cometrenderer.minecraft.blur.BlurProvider;
import com.ferra13671.cometrenderer.minecraft.font.FontType;
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
import com.ferra13671.cometrenderer.minecraft.FramebufferCapturer;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.font.FontInfo;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;

public final class UIRenderTest {
    private static GLTexture texture;
    private static TTFFont font;
    private static final BlurProvider blurProvider = new BlurProvider(BlurConfig.DEFAULT);
    private static final BlurProvider liquidGlassBlurProvider = new BlurProvider(new BlurConfig(new BlurPass[]{
            new BlurPass(new Vector2f(1, 0), 2),
            new BlurPass(new Vector2f(0, 1), 2)
    }));
    private static final FramebufferCapturer framebufferCapturer = new FramebufferCapturer();
    public static IPrimitiveBatch staticDrawer;

    public static void init() {
        texture =
                TextureLoader.FILE_ENTRY.createTextureBuilder()
                        .name("Test-texture")
                        .info(new FileEntry("texture.jpg", PathMode.INSIDE_JAR))
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();

        try {
            font = new TTFFont(
                    FontInfo.builder()
                            .font(Font.createFont(Font.TRUETYPE_FONT, UIRenderTest.class.getClassLoader().getResourceAsStream("test-font.otf")))
                            .fontSize(26)
                            .fontType(FontType.Plain)
                            .smoothFiltering(false)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        staticDrawer = new BasicTextureBatch()
                .setTexture(texture)
                .rectSized(460, 100, 100, 100, new TextureBorder(0, 0, 1, 1))
                .build();
    }

    public static void draw() {
        blurProvider.blurFrame();
        liquidGlassBlurProvider.blurFrame();
        framebufferCapturer.capture(CRM.getMainFramebuffer());
        CRM.getMainFramebuffer().bind(true);

        drawOneColorRect();
        drawMultiColorRect();
        drawTextures();
        drawRoundedRects();
        drawText();
        drawBlur();
        drawWithStencilTest();
        drawLiquidGlass();
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
        staticDrawer.tryDraw();

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

    private static void drawWithStencilTest() {
        CometRenderer.setStencil(StencilInfo.WRITE);

        int x = 200;
        int y = 520;
        int s = 25;

        new BasicRectBatch()
                .rectSized(x, y, s * 2, s * 2)
                .rectSized(x + (s * 4), y, s * 2, s * 2)
                .rectSized(x + (s * 2), y + (s * 2), s * 2, s)
                .rectSized(x + s, y + (s * 3), s * 4, s * 2)
                .rectSized(x + s, y + (s * 5), s, s)
                .rectSized(x + (s * 4), y + (s * 5), s, s)
                .build().tryDraw().close();

        CometRenderer.setStencil(StencilInfo.INVERTED_READ);

        new BasicRectBatch(() -> CometRenderer.getShaderColor().setColor(new Vector4f(1, 0, 0, 1)))
                .rectSized(x - s, y - s, s * 8, s * 8)
                .build()
                .tryDraw()
                .close();

        CometRenderer.clearStencil(0);
        CometRenderer.disableStencil();
    }

    private static void drawLiquidGlass() {
        new LiquidGlassBatch(framebufferCapturer)
                .rectSized(720, 100, 100, 100, 20, RectColors.oneColor(RenderColor.WHITE))
                .rectSized(720, 210, 100, 100, 50, RectColors.oneColor(RenderColor.WHITE))
                .build()
                .tryDraw()
                .close();

        new LiquidGlassBatch(liquidGlassBlurProvider)
                .rectSized(850, 100, 210, 210, 35, RectColors.oneColor(RenderColor.WHITE))
                .build()
                .tryDraw()
                .close();
    }
}
