package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.Getter;

public class VersionProcessor {
    private static final String directiveName = "version";
    private static final Tag<Boolean> FOUNDED_GLSL_VERSION = new Tag<>("founded-glsl-version");

    private final DirectiveExtension directiveExtension = new DirectiveExtension() {
        @Override
        public boolean supportedDirective(GlslDirective directive) {
            return directive.directiveName().equals(directiveName);
        }

        @Override
        public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
            glslFileRegistry.set(FOUNDED_GLSL_VERSION, true);

            if (directive.glslContent().getLines()[directive.lineIndex()].length() == directiveName.length())
                CometRenderer.getLogger().error(String.format("[better-compiler] Founded #version directive without value. [%s]", glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()));

            String directiveValue = directive.glslContent().getLines()[directive.lineIndex()].substring(directiveName.length() + 1).replace(" ", "");

            boolean redirectVersion = programRegistry.contains(BetterCompilerTags.GLSL_VERSION);
            boolean autoVersion = directiveValue.equals("auto");

            if (redirectVersion) {
                directive.glslContent().getLines()[directive.lineIndex()] = "#".concat(directiveName).concat(" ").concat(programRegistry.get(BetterCompilerTags.GLSL_VERSION).orElseThrow().getValue().glslVersion);
                return false;
            }

            if (autoVersion) {
                directive.glslContent().getLines()[directive.lineIndex()] = "#".concat(directiveName).concat(" ").concat(CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().glslVersion);
                return false;
            }

            return false;
        }
    };
    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-version", directiveExtension) {
        @Override
        public void processCompile(Registry shaderRegistry, Registry programRegistry) {
            if (!shaderRegistry.contains(FOUNDED_GLSL_VERSION) && programRegistry.contains(BetterCompilerTags.GLSL_VERSION)) {
                shaderRegistry.set(
                        CometTags.CONTENT,
                        GlslContent.fromString(
                                directiveName
                                        .concat(" ")
                                        .concat(programRegistry.get(BetterCompilerTags.GLSL_VERSION).orElseThrow().getValue().glslVersion)
                                        .concat("\n\n")
                                        .concat(shaderRegistry.get(CometTags.CONTENT).orElseThrow().getValue().concatLines())
                        )
                );
            }
        }
    };
}
