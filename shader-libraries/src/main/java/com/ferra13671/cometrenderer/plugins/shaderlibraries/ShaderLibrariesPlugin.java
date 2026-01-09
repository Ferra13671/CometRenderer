package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShaderLibrariesPlugin {
    private static final Tag<HashMap<String, GlslFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");
    private static final String includeLibDirective = "#include";

    public static final String SHADER_LIBRARY_FILE = "SHADER_LIB";

    static {
        CometRenderer.getRegistry().setImmutable(LIBRARIES_TAG, new HashMap<>());
        GlobalCometCompiler.addExtensions(
                (shaderRegistry, registry) -> includeShaderLibraries(shaderRegistry)
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

    public static void includeShaderLibraries(@NonNull Registry registry) {
        Map<String, UniformType<?>> uniforms = registry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true).getValue();
        String content = registry.get(CometTags.CONTENT).orElseThrow().getValue();

        boolean writeAction = false;
        boolean writeLibName = false;
        StringBuilder s = new StringBuilder();
        int i = 0;
        while (i < content.length()){
            char ch = content.charAt(i);
            i++;

            if (ch == '#') {
                s = new StringBuilder("#");
                writeAction = true;
                continue;
            }
            if (ch == '<' && writeAction) {
                writeAction = false;
                if (s.toString().equals(includeLibDirective)) {
                    writeLibName = true;
                }
                s = new StringBuilder();
                continue;
            }
            if (ch == '>' && writeLibName) {
                writeLibName = false;

                GlslFileEntry library = getShaderLibrary(s.toString()).orElseThrow();

                library.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                    if (uniforms.containsKey(s1))
                        CometRenderer.manageException(new DoubleUniformAdditionException(s1));

                    uniforms.put(s1, uniformType);
                });

                content = content.replace(includeLibDirective.concat("<").concat(s.toString()).concat(">"), library.getContent());
                i -= "#".concat(includeLibDirective).concat("<").concat(s.toString()).length();
                s = new StringBuilder();
                continue;
            }

            s.append(ch);
        }

        registry.set(CometTags.CONTENT, content);
    }
}
