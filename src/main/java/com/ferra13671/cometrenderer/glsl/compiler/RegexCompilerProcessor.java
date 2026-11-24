package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

@API(status = API.Status.EXPERIMENTAL, since = "2.8")
@UtilityClass
public class RegexCompilerProcessor {

    public void processContent(Registry glslFileRegistry, Registry builderRegistry) {
        GLSLContent content = glslFileRegistry.get(CometTags.CONTENT).orElseThrow();

        Set<RegexCompilerExtension> extensions = new HashSet<>();
        for (CompilerExtension e : CometCompiler.getExtensions())
            extensions.addAll(e.getRegexExtensions());

        if (!extensions.isEmpty())
            processExtensions(extensions, content, glslFileRegistry, builderRegistry);

        CometCompiler.removeComments(glslFileRegistry);
    }

    private void processExtensions(Set<RegexCompilerExtension> extensions, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
        Set<String> exclusions = new HashSet<>();

        boolean needProcessing = true;
        while (needProcessing) {
            needProcessing = false;

            for (RegexCompilerExtension extension : extensions) {
                Matcher matcher = extension.getPattern().matcher(content.concatLines());

                while (matcher.find()) {
                    String fullMatch = matcher.group();

                    if (exclusions.contains(fullMatch)) {
                        continue;
                    }

                    boolean processed = extension.processMatch(matcher, content, glslFileRegistry, builderRegistry);

                    if (!processed) {
                        exclusions.add(fullMatch);
                    } else {
                        needProcessing = true;
                        break;
                    }
                }

                if (needProcessing)
                    break;
            }
        }
    }
}
