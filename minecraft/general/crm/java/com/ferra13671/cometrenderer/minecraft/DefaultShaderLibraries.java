package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "2.2")
@UtilityClass
public class DefaultShaderLibraries {

    public final GlslFileEntry SHADER_COLOR = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR, CometRenderer.getColorSnippet())
            .name("shaderColor")
            .library("assets/crm/shader-libraries/shader-color.glsl")
            .build();

    //https://iquilezles.org/articles/distfunctions прикольные фигурки там да ок
    public final GlslFileEntry ROUNDED = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
            .name("rounded")
            .library("assets/crm/shader-libraries/rounded.glsl")
            .build();

    public final GlslFileEntry ROUNDED_BASE = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
            .name("rounded-base")
            .library("assets/crm/shader-libraries/rounded-base.glsl")
            .build();
}
