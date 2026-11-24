package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@API(status = API.Status.INTERNAL, since = "2.5")
@UtilityClass
public class ConstantProcessor {

    final RegexCompilerExtension regexExtension = new RegexCompilerExtension(Pattern.compile("^(?<fieldtype>\\w+)\\h+(?<fieldname>\\w+);\\h*#constant\\h*(<(?<setts>[^<>]*)>)?", Pattern.MULTILINE)) {
        @Override
        public boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            Optional<String> value = builderRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getConstant(matcher.group("fieldname"));
            String defaultValue = parseDefaultConstantValue(matcher.group("setts"));

            if (value.isEmpty() && defaultValue == null)
                CometRenderer.getLogger().error(String.format(
                        "[better-compiler] Found #constant directive, but the value for constant with name '%s' is not specified. [%s]",
                        matcher.group("fieldname"),
                        glslFileRegistry.get(CometTags.NAME).orElseThrow()
                ));

            if (defaultValue == null)
                defaultValue = "null";

            content.set(content.concatLines().replace(matcher.group(), String.join(" ", "const", matcher.group("fieldtype"), matcher.group("fieldname"), "=", value.orElse(defaultValue)).concat(";")));

            return true;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-constant") {{
        registerRegexExtensions(regexExtension);
    }};

    private String parseDefaultConstantValue(String setts) {
        if (setts != null) {
            Pattern pattern = Pattern.compile("default\\h+=\\h+(?<value>.+)");

            Matcher matcher = pattern.matcher(setts);

            String value = null;

            while (matcher.find())
                value = matcher.group("value");

            return value;
        } else
            return null;
    }
}
