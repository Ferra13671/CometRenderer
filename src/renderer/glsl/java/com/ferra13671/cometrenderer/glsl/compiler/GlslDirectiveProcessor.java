package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;

public class GlslDirectiveProcessor {

    public static void processContent(Registry glslFileRegistry, Registry programRegistry) {
        GlslContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        for (int l = 0; l < content.getLines().length; l++) {
            String line = content.getLines()[l];

            if (line.contains("#")) {
                GlslDirective directive = new GlslDirective(getDirectiveName(line), content, l);
                if (
                        processDirective(
                                directive,
                                glslFileRegistry,
                                programRegistry
                        )
                )
                    processContent(glslFileRegistry, programRegistry);
            }
        }
    }

    private static String getDirectiveName(String line) {
        int directiveIndex = line.indexOf("#");

        String directiveString = line.substring(directiveIndex + 1);
        StringBuilder directiveNameBuilder = new StringBuilder();
        for (int i = 0; i < directiveString.length(); i++) {
            char ch = directiveString.charAt(i);

            if (
                    ch == ' '
                            || ch == '<'
                            || ch == '('
                            || ch == '['
                            || ch == '{'
            )
                break;

            directiveNameBuilder.append(ch);
        }

        return directiveNameBuilder.toString();
    }

    public static boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
        for (CompilerExtension extension : GlobalCometCompiler.compilerExtensions) {
            if (extension.getDirectiveExtension() != null) {
                DirectiveExtension e = extension.getDirectiveExtension();
                if (e.supportedDirective(directive))
                    return e.processDirective(directive, glslFileRegistry, programRegistry);
            }
        }

        return false;
    }
}
