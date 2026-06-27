package com.ferra13671.cometrenderer.glsl.compiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

import java.util.Arrays;

@AllArgsConstructor
@API(status = API.Status.EXPERIMENTAL, since = "2.5")
public class GLSLContent {
    @Getter
    @Setter
    private String[] lines;

    public GLSLContent(GLSLContent content) {
        this(Arrays.copyOf(content.lines, content.lines.length));
    }

    public String concatLines() {
        return String.join("\n", lines);
    }

    public void set(String content) {
        setLines(content.split("\n"));
    }

    public static GLSLContent fromString(String string) {
        return new GLSLContent(
                string.split("\n")
        );
    }
}
