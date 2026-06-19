package com.ferra13671.cometrenderer.minecraft.font;

import lombok.Builder;
import lombok.Getter;
import org.apiguardian.api.API;

import java.awt.*;

@Builder
@Getter
@API(status = API.Status.MAINTAINED, since = "2.9")
public class FontInfo {
    private final Font font;
    @Builder.Default
    private int glyphsInMap = 500;
    @Builder.Default
    private boolean antiAliasing = true;
    @Builder.Default
    private boolean smoothFiltering = true;
    private final int fontSize;
    private final FontType fontType;
}
