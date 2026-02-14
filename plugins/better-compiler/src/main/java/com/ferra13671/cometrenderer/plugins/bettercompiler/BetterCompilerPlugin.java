package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.AbstractMethodProcessor;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.ConstantProcessor;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.ShaderLibraryProcessor;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.VersionProcessor;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

/*
TODO
    #autoBinding
    #autoPrecission
 */
public class BetterCompilerPlugin {
    private static final Tag<HashMap<String, GlslFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");
    private static final VersionProcessor VERSION_PROCESSOR = new VersionProcessor();
    private static final ConstantProcessor CONSTANT_PROCESSOR = new ConstantProcessor();
    private static final AbstractMethodProcessor ABSTRACT_METHOD_PROCESSOR = new AbstractMethodProcessor();
    private static final ShaderLibraryProcessor SHADER_LIBRARY_PROCESSOR = new ShaderLibraryProcessor();

    public static final String SHADER_LIBRARY_GLSL_FILE_ENTRY = "SHADER_LIB";

    public static void init() {
        CometRenderer.getRegistry().setImmutable(LIBRARIES_TAG, new HashMap<>());
        GlobalCometCompiler.addExtensions(
                new CompilerExtension("better-compiler-main") {
                    @Override
                    public void onCreateProgramBuilder(Registry programRegistry) {
                        programRegistry.setImmutable(BetterCompilerTags.PROGRAM_INFO, new BetterCompilerProgramInfo());
                    }
                },
                VERSION_PROCESSOR.getExtension(),
                CONSTANT_PROCESSOR.getExtension(),
                ABSTRACT_METHOD_PROCESSOR.getExtension(),
                SHADER_LIBRARY_PROCESSOR.getExtension()
        );
    }

    public static void registerShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().put(fileEntry.getName(), fileEntry);
        }
    }

    public static void unregisterShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(fileEntry.getName());
        }
    }

    public static void unregisterShaderLibraries(@NonNull String... names) {
        for (String name : names)
            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(name);
    }

    public static Optional<GlslFileEntry> getShaderLibrary(@NonNull String name) {
        return Optional.ofNullable(CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().get(name));
    }
}
