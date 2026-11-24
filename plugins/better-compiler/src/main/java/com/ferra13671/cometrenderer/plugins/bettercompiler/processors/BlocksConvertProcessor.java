package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class BlocksConvertProcessor {
    private final Pattern fieldPattern = Pattern.compile("^(?<fieldtype>\\w+)\\h+(?<fieldname>\\w+)");

    final RegexCompilerExtension inputsExtension = new RegexCompilerExtension(compileExtPattern("inputs")) {
        @Override
        public boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(matcher.group(), parseFields("in", matcher, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension outputsExtension = new RegexCompilerExtension(compileExtPattern("outputs")) {
        @Override
        public boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(matcher.group(), parseFields("out", matcher, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension uniformsExtension = new RegexCompilerExtension(compileExtPattern("uniforms")) {
        @Override
        public boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(matcher.group(), parseFields("uniform", matcher, name -> name)));
            return true;
        }
    };

    final RegexCompilerExtension inoutsExtension = new RegexCompilerExtension(compileExtPattern("inouts")) {
        @Override
        public boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            content.set(content.concatLines().replace(matcher.group(),
                    parseFields("in", matcher, name -> name.concat("_in")) + "\n"
                    + parseFields("out", matcher, name -> name)
            ));
            return true;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-interface-block") {{
        registerRegexExtensions(inputsExtension, outputsExtension, uniformsExtension, inoutsExtension);
    }};

    private String parseFields(String fieldFlag, Matcher matcher, Function<String, String> fieldNameChanger) {
        String[] fields = matcher.group("fields").replace("\n", " ").strip().split(";");

        StringBuilder fieldBuilder = new StringBuilder();
        for (String field : fields) {
            Matcher m2 = fieldPattern.matcher(field.strip());

            if (m2.find())
                fieldBuilder.append(String.join(" ", fieldFlag, m2.group("fieldtype"), fieldNameChanger.apply(m2.group("fieldname")) + ";\n"));
        }

        return fieldBuilder.toString();
    }

    private Pattern compileExtPattern(String directiveName) {
        return Pattern.compile("^\\h*#" + directiveName + "\\s*\\{\\s*(?<fields>(\\s*\\w+\\s*\\w+;\\s*)*)};", Pattern.MULTILINE);
    }
}
