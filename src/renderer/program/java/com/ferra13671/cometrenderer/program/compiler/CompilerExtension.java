package com.ferra13671.cometrenderer.program.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;

public interface CompilerExtension {

    void modify(Registry shaderRegistry, Registry programRegistry);
}
