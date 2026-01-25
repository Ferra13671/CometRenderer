package com.ferra13671.cometrenderer.glsl.compiler;

import com.ferra13671.cometrenderer.utils.tag.Registry;

public abstract class DirectiveExtension {

    /**
     * @return whether the directive is supported by the extension or not.
     */
    public abstract boolean supportedDirective(GlslDirective directive);

    /**
     * @return Whether it is necessary to process all the directives in the content again or not.
     * (Typically you should return true if your extension might add additional directives to the content that have not yet been processed.)
     */
    public abstract boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry);
}
