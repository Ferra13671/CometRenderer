package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

@API(status = API.Status.EXPERIMENTAL, since = "2.7.1")
@UtilityClass
public class RegexCompilerProcessor {

    public void processContent(Registry glslFileRegistry, Registry builderRegistry) {
        GlslContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        for (CompilerExtension e : GlobalCometCompiler.getExtensions()) {
            Set<RegexCompilerExtension> extensions = e.getRegexExtensions();

            if (!extensions.isEmpty())
                processExtensions(extensions, content, glslFileRegistry, builderRegistry);
        }

        GlobalCometCompiler.removeComments(glslFileRegistry);
    }

    private void processExtensions(Set<RegexCompilerExtension> extensions, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
        Set<String> exclusions = new HashSet<>();

        boolean needProcessing = true;
        while (needProcessing) {
            needProcessing = false;

            for (RegexCompilerExtension extension : extensions) {
                Matcher matcher = extension.getPattern().matcher(content.concatLines());

                List<MatchResult> results = matcher.results().filter(s -> !exclusions.contains(s.group())).toList();

                if (!results.isEmpty()) {
                    needProcessing = true;
                    while (!results.isEmpty()) {
                        for (MatchResult result : results) {
                            if (!extension.processMatch(result, content, glslFileRegistry, builderRegistry))
                                exclusions.add(result.group());

                            results = matcher.results().filter(s -> !exclusions.contains(s.group())).toList();
                        }
                    }
                }
            }
        }
    }
}
