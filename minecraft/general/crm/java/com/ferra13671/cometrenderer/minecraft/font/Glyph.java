package com.ferra13671.cometrenderer.minecraft.font;

import com.ferra13671.gltextureutils.atlas.TextureBorder;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.8")
public record Glyph(char symbol, TextureBorder border, float width, float height, GlyphMap instance) {
}
