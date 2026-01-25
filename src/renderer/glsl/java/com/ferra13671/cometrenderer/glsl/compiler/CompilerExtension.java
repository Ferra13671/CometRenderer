package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CompilerExtension {
    @Getter
    private final DirectiveExtension directiveExtension;

    public CompilerExtension() {
        this(null);
    }

    public void processCompile(Registry shaderRegistry, Registry programRegistry) {}
}
