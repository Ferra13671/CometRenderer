package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apiguardian.api.API;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@API(status = API.Status.EXPERIMENTAL, since = "2.8")
@AllArgsConstructor
public abstract class RegexCompilerExtension {
    @Getter
    private final Pattern pattern;

    public abstract boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry);
}
