package com.ferra13671.cometrenderer.minecraft.batch.impl.text;

import com.ferra13671.cometrenderer.minecraft.RectColors;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.batch.IPrimitiveBatch;
import com.ferra13671.cometrenderer.minecraft.batch.impl.ColoredTextureBatch;
import com.ferra13671.cometrenderer.minecraft.font.Glyph;
import com.ferra13671.cometrenderer.minecraft.font.TTFFont;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TextBatch implements IPrimitiveBatch {
    private final Map<GLTexture, ColoredTextureBatch> batches = new HashMap<>();
    private final TTFFont font;
    private final Runnable preDrawRunnable;
    private boolean built = false;

    public TextBatch() {
        this((TTFFont) null);
    }

    public TextBatch(TTFFont font) {
        this(font, null);
    }

    public TextBatch(Runnable preDrawRunnable) {
        this(null, preDrawRunnable);
    }

    public TextBatch(TTFFont font, Runnable preDrawRunnable) {
        this.font = font;
        this.preDrawRunnable = preDrawRunnable;
    }

    public TextBatch text(RenderText text) {
        float x = text.getX();
        float y = text.getY();

        for (Pair<Supplier<RenderColor>, Character[]> component : text.getText().getComponents()) {
            RenderColor color = text.getColor().multiply(component.getLeft().get());

            for (char _char : component.getRight()) {
                Glyph glyph = text.getFont() != null ? text.getFont().getGlyph(_char) : this.font.getGlyph(_char);

                if (_char == '\n') {
                    x = text.getX();
                    y += glyph.height();
                }

                ColoredTextureBatch batch = this.batches.computeIfAbsent(glyph.instance().getTexture(), texture -> new ColoredTextureBatch().setTexture(texture));

                if (text.isShadow())
                    batch.rectSized(
                            x + 1,
                            y + 1,
                            glyph.width(),
                            glyph.height(),
                            RectColors.oneColor(RenderColor.BLACK),
                            glyph.border()
                    );
                batch.rectSized(
                        x,
                        y,
                        glyph.width(),
                        glyph.height(),
                        RectColors.oneColor(color),
                        glyph.border()
                );

                x += glyph.width();
            }
        }

        return this;
    }

    @Override
    public boolean isBuilt() {
        return this.built;
    }

    @Override
    public TextBatch build() {
        this.batches.forEach((texture, batch) -> batch.build());
        this.built = true;
        return this;
    }

    @Override
    public TextBatch makeStandalone() {
        this.batches.forEach((texture, batch) -> batch.makeStandalone());
        return this;
    }

    @Override
    public TextBatch tryDraw() {
        if (this.preDrawRunnable != null)
            this.preDrawRunnable.run();

        this.batches.forEach((texture, batch) -> batch.tryDraw());
        return this;
    }

    @Override
    public void close() {
        this.batches.forEach((texture, batch) -> batch.close());
    }

    public TTFFont getFontOrNull() {
        return this.font;
    }
}
