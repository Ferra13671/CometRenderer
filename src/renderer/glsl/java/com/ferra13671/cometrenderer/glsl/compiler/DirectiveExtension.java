package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;

public abstract class DirectiveExtension {

    public abstract boolean supportedDirective(GlslDirective directive);

    public abstract String processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry);
}
