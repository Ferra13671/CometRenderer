package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apiguardian.api.API;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@API(status = API.Status.EXPERIMENTAL, since = "2.8")
@AllArgsConstructor
public abstract class RegexCompilerExtension {
    @Getter
    private final Pattern pattern;

    public abstract boolean processMatch(Matcher matcher, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry);
}
