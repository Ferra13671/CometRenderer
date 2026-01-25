package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;

/*
TODO
    #autoBinding
    #autoPrecission
    #constant
    #abstractMethod
 */
public class BetterCompilerPlugin {
    private static final VersionProcessor VERSION_PROCESSOR = new VersionProcessor();
    private static final ConstantProcessor CONSTANT_PROCESSOR = new ConstantProcessor();

    public static void init() {
        GlobalCometCompiler.addDirectiveExtensions(
                VERSION_PROCESSOR.getDirectiveExtension(),
                CONSTANT_PROCESSOR.getDirectiveExtension()
        );
        GlobalCometCompiler.addCompileExtensions(
                VERSION_PROCESSOR.getCompilerExtension()
        );
    }
}
