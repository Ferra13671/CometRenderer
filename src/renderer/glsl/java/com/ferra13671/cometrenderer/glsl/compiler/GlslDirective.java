package com.ferra13671.cometrenderer.glsl.compiler;

import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "2.5")
public record GlslDirective(String directiveName, GlslContent glslContent, int lineIndex) {
}
