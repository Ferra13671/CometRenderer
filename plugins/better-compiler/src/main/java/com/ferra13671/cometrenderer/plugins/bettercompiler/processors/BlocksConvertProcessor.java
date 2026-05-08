package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class BlocksConvertProcessor {
    private final Pattern fieldPattern = Pattern.compile("^(?<fieldtype>\\w+)\\h+(?<fieldname>\\w+)");

    final RegexCompilerExtension inputsExtension = new RegexCompilerExtension(compileExtPattern("inputs")) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(result.group(), parseFields("in", result, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension outputsExtension = new RegexCompilerExtension(compileExtPattern("outputs")) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(result.group(), parseFields("out", result, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension uniformsExtension = new RegexCompilerExtension(compileExtPattern("uniforms")) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(result.group(), parseFields("uniform", result, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension inoutsExtension = new RegexCompilerExtension(compileExtPattern("inouts")) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(result.group(),
                    parseFields("in", result, name -> name.concat("_in")) + "\n"
                    + parseFields("out", result, name -> name.concat("_out"))
            ));
            return true;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-interface-block") {{
        registerRegexExtensions(inputsExtension, outputsExtension, uniformsExtension, inoutsExtension);
    }};

    private String parseFields(String fieldFlag, MatchResult result, Function<String, String> fieldNameChanger) {
        String[] fields = result.group("fields").replace("\n", " ").strip().split(";");

        StringBuilder fieldBuilder = new StringBuilder();
        for (String field : fields) {
            Matcher matcher = fieldPattern.matcher(field.strip());

            if (matcher.find())
                fieldBuilder.append(String.join(" ", fieldFlag, matcher.group("fieldtype"), fieldNameChanger.apply(matcher.group("fieldname")) + ";\n"));
        }

        return fieldBuilder.toString();
    }

    private Pattern compileExtPattern(String directiveName) {
        return Pattern.compile("^\\h*#" + directiveName + "\\s*\\{\\s*(?<fields>(\\s*\\w+\\s*\\w+;\\s*)*)};", Pattern.MULTILINE);
    }
}
