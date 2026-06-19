package com.ferra13671.cometrenderer.minecraft.font;

import com.ferra13671.cometrenderer.CometRenderer;import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import io.netty.util.collection.IntObjectHashMap;
import lombok.Getter;
import org.apiguardian.api.API;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;

@API(status = API.Status.MAINTAINED, since = "2.8")
public class GlyphMap implements Closeable {
    private static final int CHARS_STEP = 1;

    @Getter
    private final Font font;
    @Getter
    private final int index;
    @Getter
    private final int glyphsInMap;
    private IntObjectHashMap<Glyph> glyphs;
    @Getter
    private GLTexture texture;

    public GlyphMap(Font font, int index, int glyphsInMap) {
        this.font = font;
        this.index = index;
        this.glyphsInMap = glyphsInMap;

        generate(1024, 1024);
    }

    private void generate(int width, int height) {
        IntObjectHashMap<Glyph> glyphs = new IntObjectHashMap<>();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setFont(this.font);
        FontMetrics fontMetrics = graphics.getFontMetrics();

        float xStep = 0;
        float yStep = 0;

        float charsHeight = fontMetrics.getAscent() + fontMetrics.getDescent();

        for (int i = 0; i < this.glyphsInMap; i++) {
            char _char = (char) (this.index + i);
            String s = Character.toString(_char);

            float charWidth = fontMetrics.charWidth(_char);

            if (xStep + charWidth + CHARS_STEP > width) {
                yStep += charsHeight + CHARS_STEP;
                xStep = 0;
            }

            if (yStep + charsHeight > height) {
                CometRenderer.getLogger().log(String.format("GlyphMap requires a texture with sizes greater than %sx%s. The texture size has been changed to %sx%s.", width, height, width * 2, height * 2));
                graphics.dispose();
                generate(width * 2, height * 2);
                return;
            }

            graphics.drawString(s, xStep, yStep + fontMetrics.getAscent());
            Glyph glyph = new Glyph(
                    _char,
                    new TextureBorder(
                            xStep / width,
                            yStep / height,
                            (xStep + charWidth) / width,
                            (yStep + charsHeight) / height
                    ),
                    charWidth,
                    charsHeight,
                    this
            );
            glyphs.put(_char, glyph);

            xStep += charWidth + CHARS_STEP;
        }

        this.glyphs = glyphs;

        this.texture = TextureLoader.BUFFERED_IMAGE.createTextureBuilder()
                .name(String.format("GlyphMap[%s, %s]", this.font.getName(), this.index))
                .info(bufferedImage)
                .filtering(TextureFiltering.SMOOTH)
                .wrapping(TextureWrapping.DEFAULT)
                .build();
    }

    public Glyph getGlyph(char _char) {
        return this.glyphs.get(_char);
    }

    @Override
    public void close() {
        this.texture.delete();
    }
}
