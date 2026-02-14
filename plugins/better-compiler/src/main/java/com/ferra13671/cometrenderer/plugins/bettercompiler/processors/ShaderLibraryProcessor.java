package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.DirectiveExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirective;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ShaderLibraryProcessor {
    private static final String directiveName = "#include";

    final DirectiveExtension directiveExtension = new DirectiveExtension() {
        @Override
        public boolean supportedDirective(GlslDirective directive) {
            return "#".concat(directive.directiveName()).equals(directiveName);
        }

        @Override
        public boolean processDirective(GlslDirective directive, Registry glslFileRegistry, Registry programRegistry) {
            Map<String, UniformType<?>> uniforms = programRegistry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true).getValue();

            String libsLine = directive.glslContent().getLines()[directive.lineIndex()].substring(directive.directiveName().length() + 1)
                    .replace(" ", "")
                    .replace("<", "")
                    .replace(">", "");
            String[] libs = libsLine.split(",");

            StringBuilder libsContent = new StringBuilder();
            for (String lib : libs) {
                GlslFileEntry libEntry = BetterCompilerPlugin.getShaderLibrary(lib).orElseThrow();

                libsContent.append(libEntry.getContent().concatLines()).append('\n');

                libEntry.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                    if (uniforms.containsKey(s1))
                        CometRenderer.getExceptionManager().manageException(new DoubleUniformAdditionException(s1));

                    uniforms.put(s1, uniformType);
                });
            }

            String[] libsLines = libsContent.toString().split("\n");

            String[] newLines = new String[directive.glslContent().getLines().length - 1 + libsLines.length];
            System.arraycopy(directive.glslContent().getLines(), 0, newLines, 0, directive.lineIndex());
            System.arraycopy(libsLines, 0, newLines, directive.lineIndex(), libsLines.length);
            System.arraycopy(directive.glslContent().getLines(), directive.lineIndex() + 1, newLines, directive.lineIndex() + libsLines.length, directive.glslContent().getLines().length - directive.lineIndex() - 1);

            directive.glslContent().setLines(newLines);

            return true;
        }
    };
    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-shader-library", this.directiveExtension);
}
