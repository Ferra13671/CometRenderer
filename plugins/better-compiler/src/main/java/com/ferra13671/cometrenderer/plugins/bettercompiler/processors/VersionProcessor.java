package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.Getter;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class VersionProcessor {
    private static final Tag<Boolean> FOUNDED_GLSL_VERSION = new Tag<>("founded-glsl-version");

    final RegexCompilerExtension regexExtension = new RegexCompilerExtension(Pattern.compile("^\\h*#version\\h+.*", Pattern.MULTILINE)) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            glslFileRegistry.set(FOUNDED_GLSL_VERSION, true);

            String version = result.group().replaceFirst("#version", "").strip();
            if (version.isBlank())
                CometRenderer.getLogger().error(String.format("[better-compiler] Found #version directive without value. [%s]", glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()));

            if (builderRegistry.contains(BetterCompilerTags.GLSL_VERSION)) {
                content.set(content.concatLines().replaceAll(result.group(), "#version ".concat(builderRegistry.get(BetterCompilerTags.GLSL_VERSION).orElseThrow().getValue().glslVersion)));
                return false;
            }

            if (version.equals("auto")) {
                content.set(content.concatLines().replaceAll(result.group(), "#version ".concat(CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().glslVersion)));
                return false;
            }

            return false;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-version") {

        {
            registerRegexExtensions(regexExtension);
        }

        @Override
        public void processCompile(Registry shaderRegistry, Registry programRegistry) {
            if (!shaderRegistry.contains(FOUNDED_GLSL_VERSION) && programRegistry.contains(BetterCompilerTags.GLSL_VERSION)) {
                GlslContent content = shaderRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

                content.set(
                        "#version "
                                .concat(programRegistry.get(BetterCompilerTags.GLSL_VERSION).orElseThrow().getValue().glslVersion)
                                .concat("\n\n")
                                .concat(content.concatLines())
                );
            }
        }
    };
}
