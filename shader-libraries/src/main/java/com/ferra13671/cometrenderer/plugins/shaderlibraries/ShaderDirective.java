package com.ferra13671.cometrenderer.plugins.shaderlibraries;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.program.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.exceptions.impl.DoubleUniformAdditionException;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ShaderDirective {
    @Getter
    @Setter
    private int directiveIndex;
    @Getter
    private final int directiveSize;
    private final GlslFileEntry[] libs;

    public ShaderDirective(String[] includeLibNames, int directiveIndex, int directiveSize) {
        this.directiveIndex = directiveIndex;
        this.directiveSize = directiveSize;

        this.libs = new GlslFileEntry[includeLibNames.length];
        for (int i = 0; i < includeLibNames.length; i++) {
            String libName = includeLibNames[i];

            this.libs[i] = ShaderLibrariesPlugin.getShaderLibrary(libName).orElseThrow();
        }
    }

    public void includeLibs(Registry shaderRegistry) {
        Map<String, UniformType<?>> uniforms = shaderRegistry.computeIfAbsent(CometTags.UNIFORMS, new HashMap<>(), true).getValue();
        String content = shaderRegistry.get(CometTags.CONTENT).orElseThrow().getValue();

        String preContent = content.substring(0, this.directiveIndex);
        String afterContent = content.substring(this.directiveIndex + this.directiveSize);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(preContent);

        for (int i = 0; i < this.libs.length; i++) {
            GlslFileEntry fileEntry = this.libs[i];

            contentBuilder.append(fileEntry.getContent());
            if (i < this.libs.length - 1)
                contentBuilder.append("\n");

            fileEntry.getRegistry().get(CometTags.UNIFORMS).orElseThrow().getValue().forEach((s1, uniformType) -> {
                if (uniforms.containsKey(s1))
                    CometRenderer.manageException(new DoubleUniformAdditionException(s1));

                uniforms.put(s1, uniformType);
            });
        }

        contentBuilder.append(afterContent);

        shaderRegistry.set(CometTags.CONTENT, contentBuilder.toString());
    }

    public int getIncludeLibsSize() {
        int size = this.libs.length - 1;

        for (GlslFileEntry fileEntry : this.libs)
            size += fileEntry.getContent().length();

        return size;
    }
}
