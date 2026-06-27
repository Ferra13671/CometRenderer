package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
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
    private final Tag<HashMap<String, GLSLFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");

    public final String SHADER_LIBRARY_GLSL_FILE_ENTRY = "SHADER_LIB";

    public void init() {
        CometRenderer.getRegistry().setImmutable(LIBRARIES_TAG, new HashMap<>());
        CometCompiler.addExtensions(
                new CompilerExtension("better-compiler-main") {
                    @Override
                    public void onCreateGLSLBuilder(Registry builderRegistry) {
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

    public void registerShaderLibraries(@NonNull GLSLFileEntry... fileEntries) {
        for (GLSLFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GLSLFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().put(fileEntry.getName(), fileEntry);
        }
    }

    public void unregisterShaderLibraries(@NonNull GLSLFileEntry... fileEntries) {
        for (GLSLFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_GLSL_FILE_ENTRY))
                throw new IllegalStateException(String.format("Encountered a GLSLFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_GLSL_FILE_ENTRY));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().remove(fileEntry.getName());
        }
    }

    public void unregisterShaderLibraries(@NonNull String... names) {
        for (String name : names)
            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().remove(name);
    }

    public Optional<GLSLFileEntry> getShaderLibrary(@NonNull String name) {
        return Optional.ofNullable(CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().get(name));
    }
}
