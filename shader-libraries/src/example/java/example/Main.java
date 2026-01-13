package example;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.ShaderLibrariesPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

public class Main {

    public static void main(String[] args) {
        ShaderLibrariesPlugin.registerShaderLibraries(
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib1")
                        .library("exampleLibrary1.glsl")
                        .uniform("shaderColor", UniformType.VEC4)
                        .build(),
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib2")
                        .library("exampleLibrary2.glsl")
                        .uniform("Projection", UniformType.BUFFER)
                        .uniform("modelViewMat", UniformType.MATRIX4)
                        .build(),
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib3")
                        .library("exampleLibrary3.glsl")
                        .build()
        );

        GlslFileEntry shaderEntry = CometLoaders.IN_JAR.createGlslFileEntry("exampleShader", "exampleShader.vsh");
        ShaderLibrariesPlugin.includeShaderLibraries(shaderEntry.getRegistry());
        System.out.println(shaderEntry.getContent());
    }
}
