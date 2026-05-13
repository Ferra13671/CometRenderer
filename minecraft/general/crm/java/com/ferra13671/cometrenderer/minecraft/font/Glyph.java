package com.ferra13671.cometrenderer.minecraft.font;

import com.ferra13671.gltextureutils.atlas.TextureBorder;

public record Glyph(char symbol, TextureBorder border, float width, float height, GlyphMap instance) {
}
