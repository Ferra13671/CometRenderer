package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.NonNull;

import java.util.*;

public class ShaderLibrariesPlugin {
    public static final Tag<HashMap<String, GlslFileEntry>> LIBRARIES_TAG = new Tag<>("shader-libraries");
    public static final String includeLibDirective = "#include";

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
        List<ShaderDirective> directives = getShaderDirectives(registry);
        for (int i = 0; i < directives.size(); i++) {
            ShaderDirective directive = directives.get(i);

            directive.includeLibs(registry);

            int s = directive.getIncludeLibsSize();
            for (ShaderDirective d : directives) {
                d.setDirectiveIndex(d.getDirectiveIndex() + s - directive.getDirectiveSize());
            }
        }
    }

    private static List<ShaderDirective> getShaderDirectives(Registry registry) {
        List<ShaderDirective> directives = new ArrayList<>();
        String content = registry.get(CometTags.CONTENT).orElseThrow().getValue();

        boolean writeAction = false;
        boolean writeLibNames = false;
        StringBuilder s = new StringBuilder();
        int startIndex = 0;
        int i = 0;
        while (i < content.length()) {
            char ch = content.charAt(i);
            i++;

            if (ch == '#') {
                s = new StringBuilder("#");
                writeAction = true;
                startIndex = i - 1;
                continue;
            }
            if (ch == '<' && writeAction) {
                writeAction = false;
                if (s.toString().equals(includeLibDirective)) {
                    writeLibNames = true;
                }
                s = new StringBuilder();
                continue;
            }
            if (ch == '>' && writeLibNames) {
                writeLibNames = false;

                String libs = s.toString().replace(" ", "");

                directives.add(new ShaderDirective(
                        libs.split(","),
                        startIndex,
                        i - startIndex
                ));

                s = new StringBuilder();
            }

            s.append(ch);
        }

        return directives;
    }
}
