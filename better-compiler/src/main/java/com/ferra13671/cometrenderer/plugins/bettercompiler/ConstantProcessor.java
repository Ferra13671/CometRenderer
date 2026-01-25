package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;

public class ConstantProcessor {
    private static final String directiveName = "constant";

    @Getter
    private final DirectiveExtension directiveExtension = new DirectiveExtension() {
        @Override
        public boolean supportedDirective(GlslDirective directive) {
            return directive.directiveName().equals(directiveName);
        }

        @Override
        public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
            if (!programRegistry.contains(BetterCompilerTags.CONSTANTS))
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #constant directive, but program registry does not contain the tag '%s'. [%s]",
                        BetterCompilerTags.CONSTANTS.id(),
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));

            String line = directive.glslContent().getLines()[directive.lineIndex()];

            String[] field = line.substring(0, line.indexOf(";")).split(" ");
            if (field.length != 2)
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #constant directive, but the field is not suitable for the structure: $type $name. [%s]",
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));

            //TODO ConstantContainer
            directive.glslContent().getLines()[directive.lineIndex()] = field[1];

            return false;
        }
    };
}
