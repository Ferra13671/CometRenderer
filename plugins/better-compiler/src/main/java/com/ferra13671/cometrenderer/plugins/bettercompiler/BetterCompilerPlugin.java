package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.processors.*;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.HashMap;
import java.util.Optional;

/*
TODO
    #autoBinding
    #autoPrecission
 */
@API(status = API.Status.STABLE, since = "2.5")
@UtilityClass
public class BetterCompilerPlugin {
    private final Tag<HashMap<String, GlslFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");

    public final String SHADER_LIBRARY_GLSL_FILE_ENTRY = "SHADER_LIB";

    public void init() {
        CometRenderer.getRegistry().setImmutable(LIBRARIES_TAG, new HashMap<>());
        GlobalCometCompiler.addExtensions(
                new CompilerExtension("better-compiler-main") {
                    @Override
                    public void onCreateGlslBuilder(Registry builderRegistry) {
                        builderRegistry.setImmutable(BetterCompilerTags.PROGRAM_INFO, new BetterCompilerProgramInfo());
                    }
                },
                VersionProcessor.getExtension(),
                ConstantProcessor.getExtension(),
                AbstractMethodProcessor.getExtension(),
                ShaderLibraryProcessor.getExtension(),
                BlocksConvertProcessor.getExtension()
        );
    }

    public void registerShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().put(fileEntry.getName(), fileEntry);
        }
    }

    public void unregisterShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(fileEntry.getName());
        }
    }

    public void unregisterShaderLibraries(@NonNull String... names) {
        for (String name : names)
            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(name);
    }

    public Optional<GlslFileEntry> getShaderLibrary(@NonNull String name) {
        return Optional.ofNullable(CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().get(name));
    }
}
