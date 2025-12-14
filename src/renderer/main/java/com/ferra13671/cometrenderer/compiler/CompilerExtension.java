package com.ferra13671.cometrenderer.compiler;

import com.ferra13671.cometrenderer.tag.Registry;

public interface CompilerExtension {

    void modify(Registry shaderRegistry, Registry programRegistry);
}
