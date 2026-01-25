package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.utils.tag.Registry;

/*
TODO
    #autoBinding
    #autoPrecission
    combine with 'shader-libraries' plugin?
 */
public class BetterCompilerPlugin {
    private static final VersionProcessor VERSION_PROCESSOR = new VersionProcessor();
    private static final ConstantProcessor CONSTANT_PROCESSOR = new ConstantProcessor();
    private static final AbstractMethodProcessor ABSTRACT_METHOD_PROCESSOR = new AbstractMethodProcessor();

    public static void init() {
        GlobalCometCompiler.addExtensions(
                new CompilerExtension("better-compiler-main") {
                    @Override
                    public void onCreateProgramBuilder(Registry programRegistry) {
                        programRegistry.setImmutable(BetterCompilerTags.PROGRAM_INFO, new BetterCompilerProgramInfo());
                    }
                },
                VERSION_PROCESSOR.getExtension(),
                CONSTANT_PROCESSOR.getExtension(),
                ABSTRACT_METHOD_PROCESSOR.getExtension()
        );
    }
}
