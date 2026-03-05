package com.ferra13671.cometrenderer.glsl.compiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

import java.util.Arrays;

@AllArgsConstructor
@API(status = API.Status.EXPERIMENTAL, since = "2.5")
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
