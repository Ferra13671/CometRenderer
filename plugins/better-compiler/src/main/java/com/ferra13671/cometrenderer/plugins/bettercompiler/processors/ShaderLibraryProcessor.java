package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.compiler.RegexCompilerExtension;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@API(status = API.Status.INTERNAL, since = "2.5")
@UtilityClass
public class ShaderLibraryProcessor {

    final RegexCompilerExtension regexExtension = new RegexCompilerExtension(Pattern.compile("^\\h*#include\\h*<(?<libs>[^<]*)>", Pattern.MULTILINE)) {
        @Override
        public boolean processMatch(MatchResult result, GlslContent content, Registry glslFileRegistry, Registry builderRegistry) {
            Map<String, UniformType<?>> uniforms = builderRegistry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true).getValue();
            String libsLine = result.group("libs").strip();

            if (libsLine.isEmpty()) {
                CometRenderer.getLogger().error(String.format("[better-compiler] Found empty #version directive. [%s]", glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()));
                return false;
            }

            String[] libs = libsLine.split(",");

            StringBuilder libsContent = new StringBuilder();

            for (String l : libs) {
                String lib = l.strip();

                Optional<GlslFileEntry> shaderLibOpt = BetterCompilerPlugin.getShaderLibrary(lib);

                if (shaderLibOpt.isEmpty()) {
                    CometRenderer.getLogger().error(String.format("[better-compiler] Found #include directive with unknown library '%s'. [%s]", lib, glslFileRegistry.get(CometTags.NAME).orElseThrow().getValue()));
                } else {
                    GlslFileEntry shaderLib = shaderLibOpt.get();

                    libsContent.append(shaderLib.getContent().concatLines()).append('\n');

                    shaderLib.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                        if (uniforms.containsKey(s1))
                            CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(s1));

                        uniforms.put(s1, uniformType);
                    });
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
