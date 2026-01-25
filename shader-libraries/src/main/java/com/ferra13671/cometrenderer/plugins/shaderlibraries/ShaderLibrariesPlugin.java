package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;

import java.util.*;

public class ShaderLibrariesPlugin {
    public static final Tag<HashMap<String, GlslFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");
    public static final String includeLibDirective = "include";

    public static final String SHADER_LIBRARY_FILE = "SHADER_LIB";

    static {
        CometRenderer.getRegistry().setImmutable(LIBRARIES_TAG, new HashMap<>());
        GlobalCometCompiler.addDirectiveExtensions(
                new DirectiveExtension() {
                    @Override
                    public boolean supportedDirective(GlslDirective directive) {
                        return directive.directiveName().equals(includeLibDirective);
                    }

                    @Override
                    public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
                        Map<String, UniformType<?>> uniforms = glslFileRegistry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true).getValue();

                        String libsLine = directive.glslContent().getLines()[directive.lineIndex()].substring(directive.directiveName().length() + 1)
                                .replace(" ", "")
                                .replace("<", "")
                                .replace(">", "");
                        String[] libs = libsLine.split(",");

                        StringBuilder libsContent = new StringBuilder();
                        for (String lib : libs) {
                            GlslFileEntry libEntry = getShaderLibrary(lib).orElseThrow();

                            libsContent.append(libEntry.getContent().concatLines()).append('\n');

                            libEntry.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                                if (uniforms.containsKey(s1))
                                    CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(s1));

                                uniforms.put(s1, uniformType);
                            });
                        }

                        directive.glslContent().getLines()[directive.lineIndex()] = libsContent.toString();

                        return true;
                    }
                }
        );
    }

    public static void registerShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_FILE))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_FILE));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().put(fileEntry.getName(), fileEntry);
        }
    }

    public static void unregisterShaderLibraries(@NonNull GlslFileEntry... fileEntries) {
        for (GlslFileEntry fileEntry : fileEntries) {
            if (!fileEntry.getType().equals(SHADER_LIBRARY_FILE))
                throw new IllegalStateException(String.format("Encountered a GlslFileEntry of type '%s' when '%s' was expected.", fileEntry.getType(), SHADER_LIBRARY_FILE));

            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(fileEntry.getName());
        }
    }

    public static void unregisterShaderLibraries(@NonNull String... names) {
        for (String name : names)
            CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().remove(name);
    }

    @NonNull
    public static Optional<GlslFileEntry> getShaderLibrary(String name) {
        return Optional.ofNullable(CometRenderer.getRegistry().get(LIBRARIES_TAG).orElseThrow().getValue().get(name));
    }
}
