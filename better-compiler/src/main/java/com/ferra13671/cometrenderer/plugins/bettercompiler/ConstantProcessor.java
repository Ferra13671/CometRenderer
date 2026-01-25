package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;

import java.util.Optional;

public class ConstantProcessor {
    private static final String directiveName = "#constant";

    final DirectiveExtension directiveExtension = new DirectiveExtension() {
        @Override
        public boolean supportedDirective(GlslDirective directive) {
            return "#".concat(directive.directiveName()).equals(directiveName);
        }

        @Override
        public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
            String line = directive.glslContent().getLines()[directive.lineIndex()];

            String[] field = line.substring(0, line.indexOf(";")).split(" ");
            if (field.length != 2) {
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #constant directive, but the field '%s' is not suitable for the structure: $type $name. [%s]",
                        String.join(" ", field).concat(";"),
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));
                return false;
            }

            Optional<String> value = programRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getValue().getConstant(field[1]);
            String defaultValue = getDefaultConstantValue(line);

            if (value.isEmpty() && defaultValue == null)
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #constant directive, but the value for constant with name '%s' is not specified. [%s]",
                        field[1],
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));

            if (defaultValue == null)
                defaultValue = "null";

            directive.glslContent().getLines()[directive.lineIndex()] = String.join(" ", "const", field[0], field[1], "=", value.orElse(defaultValue)).concat(";");

            return false;
        }
    };
    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-constant", this.directiveExtension);

    private String getDefaultConstantValue(String line) {
        if (line.contains("default = ")) {
            String valueString = line.substring(line.indexOf("default = ") + "default = ".length());

            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < valueString.length(); i++) {
                char ch = valueString.charAt(i);

                if (ch == '>')
                    break;

                valueBuilder.append(ch);
            }

            return valueBuilder.toString().replace(" ", "");
        } else return null;
    }
}
