package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.glsl.GLSLLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GLShaderLibraryBuilder;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "2.2")
@UtilityClass
public class DefaultShaderLibraries {

    public final GLSLFileEntry SHADER_COLOR = new GLShaderLibraryBuilder<>(GLSLLoader.IN_JAR, CometRenderer.getColorSnippet())
            .name("shaderColor")
            .library("assets/crm/shader-libraries/shader-color.glsl")
            .singleIncludeOnly()
            .build();

    //https://iquilezles.org/articles/distfunctions прикольные фигурки там да ок
    public final GLSLFileEntry ROUNDED = new GLShaderLibraryBuilder<>(GLSLLoader.IN_JAR)
            .name("rounded")
            .library("assets/crm/shader-libraries/rounded.glsl")
            .singleIncludeOnly()
            .build();

    public final GLSLFileEntry ROUNDED_BASE = new GLShaderLibraryBuilder<>(GLSLLoader.IN_JAR)
            .name("rounded-base")
            .library("assets/crm/shader-libraries/rounded-base.glsl")
            .singleIncludeOnly()
            .build();
}
