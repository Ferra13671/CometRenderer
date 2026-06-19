package com.ferra13671.cometrenderer.minecraft.font;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

import java.awt.*;

@AllArgsConstructor
@API(status = API.Status.MAINTAINED, since = "2.9")
public enum FontType {
    Plain(Font.PLAIN),
    Bold(Font.BOLD),
    Italic(Font.ITALIC);

    public final int id;
}
