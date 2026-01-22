package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.GlShaderLibraryBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultShaderLibraries {

    public final GlslFileEntry SHADER_COLOR = new GlShaderLibraryBuilder<>(CometLoaders.STRING, CometRenderer.getColorSnippet())
            .name("shaderColor")
            .library(
                    """
                    uniform vec4 shaderColor;
                    """
            )
            .build();

    //https://iquilezles.org/articles/distfunctions прикольные фигурки там да ок
    public final GlslFileEntry ROUNDED = new GlShaderLibraryBuilder<>(CometLoaders.STRING)
            .name("rounded")
            .library(
                    """
                    float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
                        return length(max(abs(centerPosition) - size + radius, 0.)) - radius;
                    }
                    """
            )
            .build();
}
