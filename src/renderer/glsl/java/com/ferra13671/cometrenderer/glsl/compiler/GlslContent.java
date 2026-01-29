package com.ferra13671.cometrenderer.glsl.compiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@AllArgsConstructor
public class GlslContent {
    @Getter
    @Setter
    private String[] lines;

    public GlslContent(GlslContent content) {
        this(Arrays.copyOf(content.lines, content.lines.length));
    }

    public String concatLines() {
        return String.join("\n", lines);
    }

    public static GlslContent fromString(String string) {
        return new GlslContent(
                string.split("\n")
        );
    }
}
