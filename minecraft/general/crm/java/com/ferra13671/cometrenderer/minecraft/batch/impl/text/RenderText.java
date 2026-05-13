package com.ferra13671.cometrenderer.minecraft.batch.impl.text;

import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.cometrenderer.minecraft.font.FormattedText;
import com.ferra13671.cometrenderer.minecraft.font.TTFFont;
import lombok.Getter;

@Getter
public class RenderText {
    private FormattedText text;
    private float x;
    private float y;
    private RenderColor color = RenderColor.WHITE;
    private boolean shadow = false;
    private TTFFont font;

    public RenderText(String s, float x, float y) {
        this(new FormattedText(s), x, y);
    }

    public RenderText(FormattedText text, float x, float y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public RenderText withText(String s) {
        return withText(new FormattedText(s));
    }

    public RenderText withText(FormattedText text) {
        this.text = text;
        return this;
    }

    public RenderText withX(float x) {
        this.x = x;
        return this;
    }

    public RenderText withY(float y) {
        this.y = y;
        return this;
    }

    public RenderText withColor(RenderColor color) {
        this.color = color;
        return this;
    }

    public RenderText withShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public RenderText withFont(TTFFont font) {
        this.font = font;
        return this;
    }
}
