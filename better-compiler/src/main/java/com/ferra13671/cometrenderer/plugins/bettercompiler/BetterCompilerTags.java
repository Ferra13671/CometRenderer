package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Tag;

import java.util.HashMap;

public final class BetterCompilerTags {
    public static final Tag<GLVersion> GLSL_VERSION = new Tag<>("glsl-version");
    public static final Tag<HashMap<String, String>> CONSTANTS = new Tag<>("constants");
}
