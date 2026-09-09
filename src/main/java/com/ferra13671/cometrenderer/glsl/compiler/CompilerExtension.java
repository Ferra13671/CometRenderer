package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apiguardian.api.API;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@API(status = API.Status.EXPERIMENTAL, since = "1.9")
public class CompilerExtension {
    @Getter
    private final String name;
    private final Set<RegexCompilerExtension> regexExtensions = new HashSet<>();

    public void processCompile(Registry shaderRegistry, Registry programRegistry) {}

    public void onCreateGLSLBuilder(Registry builderRegistry) {}

    protected void registerRegexExtensions(RegexCompilerExtension... extensions) {
        this.regexExtensions.addAll(List.of(extensions));
    }

    public Set<RegexCompilerExtension> getRegexExtensions() {
        return Collections.unmodifiableSet(this.regexExtensions);
    }
}
