package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.plugins.bettercompiler.utils.StringExtension;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;

import java.util.Optional;

@ExtensionMethod(value = {StringExtension.class})
public class AbstractMethodProcessor {
    private static final String directiveName = "#abstractMethod";

    final DirectiveExtension directiveExtension = new DirectiveExtension() {
        @Override
        public boolean supportedDirective(GlslDirective directive) {
            return "#".concat(directive.directiveName()).equals(directiveName);
        }

        @Override
        public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
            String line = directive.glslContent().getLines()[directive.lineIndex()];

            if (
                    line.charCount('(') != 1
                    || line.charCount(')') != 1
            ) {
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #abstractmethod directive, but the method '%s' is not suitable for the structure: $type $name($params). [%s]",
                        line.substring(0, line.indexOf(";")),
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));
                return false;
            }

            String[] method = new String[2];
            method[0] = line.substring(0, line.indexOf(" "));
            method[1] = line.substring(line.indexOf(" ") + 1, line.indexOf(")") + 1);

            String methodName = method[1].substring(0, method[1].indexOf("("));
            Optional<String> methodContent = programRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getValue().getMethodContent(methodName);

            String content = "";

            if (methodContent.isEmpty()) {
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #abstractMethod directive, but the content for method with name '%s' is not specified. [%s]",
                        methodName,
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));
            } else
                content = methodContent.get();

            content = processContent(content);

            directive.glslContent().getLines()[directive.lineIndex()] =
                    method[0].concat(" ").concat(method[1]).concat(" {").concat("\n")
                            .concat(
                                    content
                            )
                            .concat("\n}");

            return false;
        }
    };
    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compile-abstract-method", this.directiveExtension);

    private String processContent(String content) {
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++)
            lines[i] = "    ".concat(lines[i]);

        return String.join("\n", lines);
    }
}
