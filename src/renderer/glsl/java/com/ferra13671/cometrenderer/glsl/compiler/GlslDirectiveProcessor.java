package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "2.5")
public class GlslDirectiveProcessor {

    public static void processContent(Registry glslFileRegistry, Registry builderRegistry) {
        GlslContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        for (int l = 0; l < content.getLines().length; l++) {
            String line = content.getLines()[l];

            if (line.contains("#")) {
                GlslDirective directive = new GlslDirective(getDirectiveName(line), content, l);
                if (
                        processDirective(
                                directive,
                                glslFileRegistry,
                                builderRegistry
                        )
                ) {
                    GlobalCometCompiler.removeComments(glslFileRegistry);
                    processContent(glslFileRegistry, builderRegistry);
                }
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

    public static boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry builderRegistry) {
        for (CompilerExtension extension : GlobalCometCompiler.getExtensions()) {
            if (extension.getDirectiveExtension() != null) {
                DirectiveExtension e = extension.getDirectiveExtension();
                if (e.supportedDirective(directive))
                    return e.processDirective(directive, glslFileRegistry, builderRegistry);
            }
        }

        return false;
    }
}
