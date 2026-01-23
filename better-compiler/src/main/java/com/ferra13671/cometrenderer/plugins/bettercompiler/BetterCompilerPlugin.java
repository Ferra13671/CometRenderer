package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;

/*
TODO
    #autoBinding
    #autoPrecission
    #constant
    #abstractMethod
 */
public class BetterCompilerPlugin {
    private static final String glslVersionDirective = "#version";
    private static final Tag<Boolean> FINDED_GLSL_VERSION = new Tag<>("redirected-glsl-version");
    public static final Tag<GLVersion> GLSL_VERSION = new Tag<>("glsl-version");

    static {
        GlobalCometCompiler.addDirectiveExtensions(
                new DirectiveExtension() {
                    @Override
                    public boolean supportedDirective(GlslDirective directive) {
                        return directive.directiveName().equals(glslVersionDirective);
                    }

                    @Override
                    public String processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
                        glslFileRegistry.set(FINDED_GLSL_VERSION, true);

                        if (directive.line().length() == glslVersionDirective.length())
                            throw new IllegalStateException(String.format("Founded #version directive without value [%s]", glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()));

                        String directiveValue = directive.line().substring(glslVersionDirective.length() + 1).replace(" ", "");

                        boolean redirectVersion = programRegistry.contains(GLSL_VERSION);
                        boolean autoVersion = directiveValue.equals("auto");

                        if (redirectVersion)
                            return glslVersionDirective.concat(" ").concat(programRegistry.get(GLSL_VERSION).orElseThrow().getValue().glslVersion);

                        if (autoVersion)
                            return glslVersionDirective.concat(" ").concat(CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue().glslVersion);

                        return directive.line();
                    }
                }
        );
        GlobalCometCompiler.addCompileExtensions(
                (shaderRegistry, programRegistry) -> {
                    if (!shaderRegistry.contains(FINDED_GLSL_VERSION) && programRegistry.contains(GLSL_VERSION)) {
                        shaderRegistry.set(
                                CometTags.CONTENT,
                                glslVersionDirective
                                        .concat(" ")
                                        .concat(programRegistry.get(GLSL_VERSION).orElseThrow().getValue().glslVersion)
                                        .concat("\n\n")
                                        .concat(shaderRegistry.get(CometTags.CONTENT).orElseThrow().getValue())
                        );
                    }
                }
        );
    }
}
