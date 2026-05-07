package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class AbstractMethodProcessor {

    final RegexCompilerExtension regexExtension = new RegexCompilerExtension(Pattern.compile("^(?<methodtype>\\w+)\\h+(?<methodname>\\w+)\\h*\\((?<args>.+)?\\);\\h+#abstractMethod", Pattern.MULTILINE)) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            Optional<String> methodContentOpt = builderRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getValue().getMethodContent(result.group("methodname"));

            String methodContent = "";

            if (methodContentOpt.isEmpty()) {
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Founded #abstractMethod directive, but the content for method with name '%s' is not specified. [%s]",
                        result.group("methodname"),
                        glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()
                ));
            } else
                methodContent = methodContentOpt.get();

            methodContent = processContent(methodContent);

            content.set(content.concatLines().replace(
                    result.group(),
                    result.group("methodtype")
                            .concat(" ").concat(result.group("methodname"))
                            .concat("(").concat(Optional.ofNullable(result.group("args")).orElse("")).concat(") {\n")
                            .concat(methodContent)
                            .concat("\n}")
            ));

            return true;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compile-abstract-method") {{
        registerRegexExtensions(regexExtension);
    }};

    private String processContent(String content) {
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++)
            lines[i] = "    ".concat(lines[i]);

        return String.join("\n", lines);
    }
}
