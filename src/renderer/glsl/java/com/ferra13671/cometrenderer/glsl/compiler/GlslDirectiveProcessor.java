package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;

public class GlslDirectiveProcessor {

    public static void processContent(Registry glslFileRegistry, Registry programRegistry) {
        String content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        boolean listen = false;
        StringBuilder s = new StringBuilder();
        StringBuilder lineBuilder = new StringBuilder();
        int startLineIndex = 0;
        int startDirectiveIndex = 0;
        int i = 0;
        while (i < content.length()) {
            char ch = content.charAt(i);
            if (ch != '\n')
                lineBuilder.append(ch);
            i++;

            if (ch == '#') {
                s = new StringBuilder("#");
                listen = true;
                startDirectiveIndex = i - 1;
                continue;
            }
            if (listen) {
                switch (ch) {
                    case '\n', ' ', '<', '(', '[', '{' -> {
                        listen = false;

                        String directiveName = s.toString();

                        int endLineIndex;

                        String line;
                        if (ch == '\n') {
                            line = lineBuilder.toString();
                            endLineIndex = i - 1;
                        } else {
                            s.append(ch);
                            int a = i;
                            for (; a < content.length(); a++) {
                                ch = content.charAt(a);

                                if (ch == '\n') {
                                    break;
                                } else
                                    lineBuilder.append(ch);
                            }

                            line = lineBuilder.toString();
                            endLineIndex = a;
                        }

                        GlslDirective directive = new GlslDirective(line, directiveName, startDirectiveIndex);
                        String processedLine = processDirective(directive, glslFileRegistry, programRegistry);

                        if (!directive.line().equals(processedLine)) {
                            String beforeString = content.substring(0, startLineIndex);
                            String afterString = content.substring(endLineIndex);

                            glslFileRegistry.set(
                                    CometTags.CONTENT,
                                    beforeString.concat(processedLine).concat(afterString)
                            );

                            processContent(glslFileRegistry, programRegistry);

                            return;
                        }
                    }
                }

                s.append(ch);
            } else {
                if (ch == '\n') {
                    startLineIndex = i;
                    s = new StringBuilder();
                    lineBuilder = new StringBuilder();
                }
            }
        }
    }

    public static String processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
        for (DirectiveExtension extension : GlobalCometCompiler.directiveExtensions) {
            if (extension.supportedDirective(directive))
                return extension.processDirective(directive, glslFileRegistry, programRegistry);
        }
        return directive.line();
    }
}
