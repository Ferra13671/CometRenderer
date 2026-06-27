package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@API(status = API.Status.INTERNAL, since = "2.5")
@UtilityClass
public class ShaderLibraryProcessor {
    public static final Tag<List<String>> INCLUDED_LIBRARIES = new Tag<>("included-libraries");
    public static final Tag<Boolean> SINGLE_INCLUDE_ONLY = new Tag<>("single-include-only");

    final RegexCompilerExtension regexExtension = new RegexCompilerExtension(Pattern.compile("^\\h*#include\\h*<(?<libs>[^<>]*)>", Pattern.MULTILINE)) {
        @Override
        public boolean processMatch(MatchResult result, GLSLContent content, Registry glslFileRegistry, Registry builderRegistry) {
            List<String> includedLibraries = glslFileRegistry.computeIfAbsent(INCLUDED_LIBRARIES, new ArrayList<>(), true);
            Map<String, UniformType<?>> uniforms = builderRegistry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true);
            String libsLine = result.group("libs").strip();

            if (libsLine.isEmpty()) {
                CometRenderer.getLogger().error(String.format("[better-compiler] Found empty #version directive. [%s]", glslFileRegistry.get(CometTags.NAME).orElseThrow()));
                return false;
            }

            String[] libs = libsLine.split(",");

            StringBuilder libsContent = new StringBuilder();

            for (String l : libs) {
                String lib = l.strip();

                Optional<GLSLFileEntry> shaderLibOpt = BetterCompilerPlugin.getShaderLibrary(lib);

                if (shaderLibOpt.isEmpty()) {
                    CometRenderer.getLogger().error(String.format("[better-compiler] Found #include directive with unknown library '%s'. [%s]", lib, glslFileRegistry.get(CometTags.NAME).orElseThrow()));
                } else {
                    GLSLFileEntry shaderLib = shaderLibOpt.get();

                    if (shaderLib.getRegistry().get(SINGLE_INCLUDE_ONLY).orElseThrow() && includedLibraries.contains(shaderLib.getName())) {
                        CometRenderer.getLogger().warn(String.format("[better-compiler] Shader library '%s' cannot be re-included because it is prohibited by the library itself. [%s]", lib, glslFileRegistry.get(CometTags.NAME).orElseThrow()));
                        continue;
                    }

                    libsContent.append(shaderLib.getContent().concatLines()).append('\n');

                    shaderLib.getRegistry().get(CometTags.UNIFORMS).orElseThrow().forEach((s1, uniformType) -> {
                        if (uniforms.containsKey(s1))
                            ErrorHandlers.onDoubleUniformAddition(s1);

                        uniforms.put(s1, uniformType);
                    });

                    includedLibraries.add(shaderLib.getName());
                }
            }

            if (!libsContent.isEmpty()) {
                content.set(content.concatLines().replace(result.group(), libsContent.toString()));
                return true;
            }

            return false;
        }
    };

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-shader-library") {{
        registerRegexExtensions(regexExtension);
    }};
}
